import { element, by, ElementFinder } from 'protractor';

export class GroceryListComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-grocery-list div table .btn-danger'));
    title = element.all(by.css('jhi-grocery-list div h2#page-heading span')).first();

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

export class GroceryListUpdatePage {
    pageTitle = element(by.id('jhi-grocery-list-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    storeItemSelect = element(by.id('field_storeItem'));
    familyMemberSelect = element(by.id('field_familyMember'));
    familyGroupSelect = element(by.id('field_familyGroup'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
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

    async familyMemberSelectLastOption() {
        await this.familyMemberSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async familyMemberSelectOption(option) {
        await this.familyMemberSelect.sendKeys(option);
    }

    getFamilyMemberSelect(): ElementFinder {
        return this.familyMemberSelect;
    }

    async getFamilyMemberSelectedOption() {
        return this.familyMemberSelect.element(by.css('option:checked')).getText();
    }

    async familyGroupSelectLastOption() {
        await this.familyGroupSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async familyGroupSelectOption(option) {
        await this.familyGroupSelect.sendKeys(option);
    }

    getFamilyGroupSelect(): ElementFinder {
        return this.familyGroupSelect;
    }

    async getFamilyGroupSelectedOption() {
        return this.familyGroupSelect.element(by.css('option:checked')).getText();
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

export class GroceryListDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-groceryList-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-groceryList'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
