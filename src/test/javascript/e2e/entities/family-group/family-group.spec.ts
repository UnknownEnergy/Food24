/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FamilyGroupComponentsPage, FamilyGroupDeleteDialog, FamilyGroupUpdatePage } from './family-group.page-object';

const expect = chai.expect;

describe('FamilyGroup e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let familyGroupUpdatePage: FamilyGroupUpdatePage;
    let familyGroupComponentsPage: FamilyGroupComponentsPage;
    let familyGroupDeleteDialog: FamilyGroupDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load FamilyGroups', async () => {
        await navBarPage.goToEntity('family-group');
        familyGroupComponentsPage = new FamilyGroupComponentsPage();
        await browser.wait(ec.visibilityOf(familyGroupComponentsPage.title), 5000);
        expect(await familyGroupComponentsPage.getTitle()).to.eq('food24App.familyGroup.home.title');
    });

    it('should load create FamilyGroup page', async () => {
        await familyGroupComponentsPage.clickOnCreateButton();
        familyGroupUpdatePage = new FamilyGroupUpdatePage();
        expect(await familyGroupUpdatePage.getPageTitle()).to.eq('food24App.familyGroup.home.createOrEditLabel');
        await familyGroupUpdatePage.cancel();
    });

    it('should create and save FamilyGroups', async () => {
        const nbButtonsBeforeCreate = await familyGroupComponentsPage.countDeleteButtons();

        await familyGroupComponentsPage.clickOnCreateButton();
        await promise.all([familyGroupUpdatePage.setNameInput('name')]);
        expect(await familyGroupUpdatePage.getNameInput()).to.eq('name');
        await familyGroupUpdatePage.save();
        expect(await familyGroupUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await familyGroupComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last FamilyGroup', async () => {
        const nbButtonsBeforeDelete = await familyGroupComponentsPage.countDeleteButtons();
        await familyGroupComponentsPage.clickOnLastDeleteButton();

        familyGroupDeleteDialog = new FamilyGroupDeleteDialog();
        expect(await familyGroupDeleteDialog.getDialogTitle()).to.eq('food24App.familyGroup.delete.question');
        await familyGroupDeleteDialog.clickOnConfirmButton();

        expect(await familyGroupComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
