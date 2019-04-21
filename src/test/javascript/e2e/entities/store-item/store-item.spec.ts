/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { StoreItemComponentsPage, StoreItemDeleteDialog, StoreItemUpdatePage } from './store-item.page-object';

const expect = chai.expect;

describe('StoreItem e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let storeItemUpdatePage: StoreItemUpdatePage;
    let storeItemComponentsPage: StoreItemComponentsPage;
    let storeItemDeleteDialog: StoreItemDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load StoreItems', async () => {
        await navBarPage.goToEntity('store-item');
        storeItemComponentsPage = new StoreItemComponentsPage();
        await browser.wait(ec.visibilityOf(storeItemComponentsPage.title), 5000);
        expect(await storeItemComponentsPage.getTitle()).to.eq('food24App.storeItem.home.title');
    });

    it('should load create StoreItem page', async () => {
        await storeItemComponentsPage.clickOnCreateButton();
        storeItemUpdatePage = new StoreItemUpdatePage();
        expect(await storeItemUpdatePage.getPageTitle()).to.eq('food24App.storeItem.home.createOrEditLabel');
        await storeItemUpdatePage.cancel();
    });

    it('should create and save StoreItems', async () => {
        const nbButtonsBeforeCreate = await storeItemComponentsPage.countDeleteButtons();

        await storeItemComponentsPage.clickOnCreateButton();
        await promise.all([storeItemUpdatePage.setNameInput('name')]);
        expect(await storeItemUpdatePage.getNameInput()).to.eq('name');
        await storeItemUpdatePage.save();
        expect(await storeItemUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await storeItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last StoreItem', async () => {
        const nbButtonsBeforeDelete = await storeItemComponentsPage.countDeleteButtons();
        await storeItemComponentsPage.clickOnLastDeleteButton();

        storeItemDeleteDialog = new StoreItemDeleteDialog();
        expect(await storeItemDeleteDialog.getDialogTitle()).to.eq('food24App.storeItem.delete.question');
        await storeItemDeleteDialog.clickOnConfirmButton();

        expect(await storeItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
