import { element, by, ElementFinder } from 'protractor';

export class StoreItemInstanceComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-store-item-instance div table .btn-danger'));
    title = element.all(by.css('jhi-store-item-instance div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class StoreItemInstanceUpdatePage {
    pageTitle = element(by.id('jhi-store-item-instance-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    priceInput = element(by.id('field_price'));
    storeItemSelect = element(by.id('field_storeItem'));
    storeSelect = element(by.id('field_store'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setPriceInput(price) {
        await this.priceInput.sendKeys(price);
    }

    async getPriceInput() {
        return this.priceInput.getAttribute('value');
    }

    async storeItemSelectLastOption() {
        await this.storeItemSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async storeItemSelectOption(option) {
        await this.storeItemSelect.sendKeys(option);
    }

    getStoreItemSelect(): ElementFinder {
        return this.storeItemSelect;
    }

    async getStoreItemSelectedOption() {
        return this.storeItemSelect.element(by.css('option:checked')).getText();
    }

    async storeSelectLastOption() {
        await this.storeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async storeSelectOption(option) {
        await this.storeSelect.sendKeys(option);
    }

    getStoreSelect(): ElementFinder {
        return this.storeSelect;
    }

    async getStoreSelectedOption() {
        return this.storeSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class StoreItemInstanceDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-storeItemInstance-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-storeItemInstance'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
