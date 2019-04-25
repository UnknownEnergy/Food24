/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FamilyMemberComponentsPage, FamilyMemberDeleteDialog, FamilyMemberUpdatePage } from './family-member.page-object';

const expect = chai.expect;

describe('FamilyMember e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let familyMemberUpdatePage: FamilyMemberUpdatePage;
    let familyMemberComponentsPage: FamilyMemberComponentsPage;
    let familyMemberDeleteDialog: FamilyMemberDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load FamilyMembers', async () => {
        await navBarPage.goToEntity('family-member');
        familyMemberComponentsPage = new FamilyMemberComponentsPage();
        await browser.wait(ec.visibilityOf(familyMemberComponentsPage.title), 5000);
        expect(await familyMemberComponentsPage.getTitle()).to.eq('food24App.familyMember.home.title');
    });

    it('should load create FamilyMember page', async () => {
        await familyMemberComponentsPage.clickOnCreateButton();
        familyMemberUpdatePage = new FamilyMemberUpdatePage();
        expect(await familyMemberUpdatePage.getPageTitle()).to.eq('food24App.familyMember.home.createOrEditLabel');
        await familyMemberUpdatePage.cancel();
    });

    it('should create and save FamilyMembers', async () => {
        const nbButtonsBeforeCreate = await familyMemberComponentsPage.countDeleteButtons();

        await familyMemberComponentsPage.clickOnCreateButton();
        await promise.all([
            familyMemberUpdatePage.userSelectLastOption(),
            // familyMemberUpdatePage.familyGroupSelectLastOption(),
            familyMemberUpdatePage.locationSelectLastOption()
        ]);
        await familyMemberUpdatePage.save();
        expect(await familyMemberUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await familyMemberComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last FamilyMember', async () => {
        const nbButtonsBeforeDelete = await familyMemberComponentsPage.countDeleteButtons();
        await familyMemberComponentsPage.clickOnLastDeleteButton();

        familyMemberDeleteDialog = new FamilyMemberDeleteDialog();
        expect(await familyMemberDeleteDialog.getDialogTitle()).to.eq('food24App.familyMember.delete.question');
        await familyMemberDeleteDialog.clickOnConfirmButton();

        expect(await familyMemberComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
