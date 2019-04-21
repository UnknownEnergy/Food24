/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { GroceryListComponentsPage, GroceryListDeleteDialog, GroceryListUpdatePage } from './grocery-list.page-object';

const expect = chai.expect;

describe('GroceryList e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let groceryListUpdatePage: GroceryListUpdatePage;
    let groceryListComponentsPage: GroceryListComponentsPage;
    let groceryListDeleteDialog: GroceryListDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load GroceryLists', async () => {
        await navBarPage.goToEntity('grocery-list');
        groceryListComponentsPage = new GroceryListComponentsPage();
        await browser.wait(ec.visibilityOf(groceryListComponentsPage.title), 5000);
        expect(await groceryListComponentsPage.getTitle()).to.eq('food24App.groceryList.home.title');
    });

    it('should load create GroceryList page', async () => {
        await groceryListComponentsPage.clickOnCreateButton();
        groceryListUpdatePage = new GroceryListUpdatePage();
        expect(await groceryListUpdatePage.getPageTitle()).to.eq('food24App.groceryList.home.createOrEditLabel');
        await groceryListUpdatePage.cancel();
    });

    it('should create and save GroceryLists', async () => {
        const nbButtonsBeforeCreate = await groceryListComponentsPage.countDeleteButtons();

        await groceryListComponentsPage.clickOnCreateButton();
        await promise.all([
            groceryListUpdatePage.setNameInput('name'),
            // groceryListUpdatePage.storeItemSelectLastOption(),
            groceryListUpdatePage.familyMemberSelectLastOption(),
            groceryListUpdatePage.familyGroupSelectLastOption()
        ]);
        expect(await groceryListUpdatePage.getNameInput()).to.eq('name');
        await groceryListUpdatePage.save();
        expect(await groceryListUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await groceryListComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last GroceryList', async () => {
        const nbButtonsBeforeDelete = await groceryListComponentsPage.countDeleteButtons();
        await groceryListComponentsPage.clickOnLastDeleteButton();

        groceryListDeleteDialog = new GroceryListDeleteDialog();
        expect(await groceryListDeleteDialog.getDialogTitle()).to.eq('food24App.groceryList.delete.question');
        await groceryListDeleteDialog.clickOnConfirmButton();

        expect(await groceryListComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
