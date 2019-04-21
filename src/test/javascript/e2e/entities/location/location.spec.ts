/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { LocationComponentsPage, LocationDeleteDialog, LocationUpdatePage } from './location.page-object';

const expect = chai.expect;

describe('Location e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let locationUpdatePage: LocationUpdatePage;
    let locationComponentsPage: LocationComponentsPage;
    let locationDeleteDialog: LocationDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Locations', async () => {
        await navBarPage.goToEntity('location');
        locationComponentsPage = new LocationComponentsPage();
        await browser.wait(ec.visibilityOf(locationComponentsPage.title), 5000);
        expect(await locationComponentsPage.getTitle()).to.eq('food24App.location.home.title');
    });

    it('should load create Location page', async () => {
        await locationComponentsPage.clickOnCreateButton();
        locationUpdatePage = new LocationUpdatePage();
        expect(await locationUpdatePage.getPageTitle()).to.eq('food24App.location.home.createOrEditLabel');
        await locationUpdatePage.cancel();
    });

    it('should create and save Locations', async () => {
        const nbButtonsBeforeCreate = await locationComponentsPage.countDeleteButtons();

        await locationComponentsPage.clickOnCreateButton();
        await promise.all([
            locationUpdatePage.setLatitudeInput('5'),
            locationUpdatePage.setLongitudeInput('5'),
            locationUpdatePage.setDescriptionInput('description')
        ]);
        expect(await locationUpdatePage.getLatitudeInput()).to.eq('5');
        expect(await locationUpdatePage.getLongitudeInput()).to.eq('5');
        expect(await locationUpdatePage.getDescriptionInput()).to.eq('description');
        await locationUpdatePage.save();
        expect(await locationUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await locationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Location', async () => {
        const nbButtonsBeforeDelete = await locationComponentsPage.countDeleteButtons();
        await locationComponentsPage.clickOnLastDeleteButton();

        locationDeleteDialog = new LocationDeleteDialog();
        expect(await locationDeleteDialog.getDialogTitle()).to.eq('food24App.location.delete.question');
        await locationDeleteDialog.clickOnConfirmButton();

        expect(await locationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
