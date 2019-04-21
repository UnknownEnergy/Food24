/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
    StoreItemInstanceComponentsPage,
    StoreItemInstanceDeleteDialog,
    StoreItemInstanceUpdatePage
} from './store-item-instance.page-object';

const expect = chai.expect;

describe('StoreItemInstance e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let storeItemInstanceUpdatePage: StoreItemInstanceUpdatePage;
    let storeItemInstanceComponentsPage: StoreItemInstanceComponentsPage;
    let storeItemInstanceDeleteDialog: StoreItemInstanceDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load StoreItemInstances', async () => {
        await navBarPage.goToEntity('store-item-instance');
        storeItemInstanceComponentsPage = new StoreItemInstanceComponentsPage();
        await browser.wait(ec.visibilityOf(storeItemInstanceComponentsPage.title), 5000);
        expect(await storeItemInstanceComponentsPage.getTitle()).to.eq('food24App.storeItemInstance.home.title');
    });

    it('should load create StoreItemInstance page', async () => {
        await storeItemInstanceComponentsPage.clickOnCreateButton();
        storeItemInstanceUpdatePage = new StoreItemInstanceUpdatePage();
        expect(await storeItemInstanceUpdatePage.getPageTitle()).to.eq('food24App.storeItemInstance.home.createOrEditLabel');
        await storeItemInstanceUpdatePage.cancel();
    });

    it('should create and save StoreItemInstances', async () => {
        const nbButtonsBeforeCreate = await storeItemInstanceComponentsPage.countDeleteButtons();

        await storeItemInstanceComponentsPage.clickOnCreateButton();
        await promise.all([
            storeItemInstanceUpdatePage.setPriceInput('5'),
            storeItemInstanceUpdatePage.storeItemSelectLastOption(),
            storeItemInstanceUpdatePage.storeSelectLastOption()
        ]);
        expect(await storeItemInstanceUpdatePage.getPriceInput()).to.eq('5');
        await storeItemInstanceUpdatePage.save();
        expect(await storeItemInstanceUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await storeItemInstanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last StoreItemInstance', async () => {
        const nbButtonsBeforeDelete = await storeItemInstanceComponentsPage.countDeleteButtons();
        await storeItemInstanceComponentsPage.clickOnLastDeleteButton();

        storeItemInstanceDeleteDialog = new StoreItemInstanceDeleteDialog();
        expect(await storeItemInstanceDeleteDialog.getDialogTitle()).to.eq('food24App.storeItemInstance.delete.question');
        await storeItemInstanceDeleteDialog.clickOnConfirmButton();

        expect(await storeItemInstanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
