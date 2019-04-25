import { element, by, ElementFinder } from 'protractor';

export class FamilyMemberComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-family-member div table .btn-danger'));
    title = element.all(by.css('jhi-family-member div h2#page-heading span')).first();

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

export class FamilyMemberUpdatePage {
    pageTitle = element(by.id('jhi-family-member-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    userSelect = element(by.id('field_user'));
    familyGroupSelect = element(by.id('field_familyGroup'));
    locationSelect = element(by.id('field_location'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async userSelectLastOption() {
        await this.userSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async userSelectOption(option) {
        await this.userSelect.sendKeys(option);
    }

    getUserSelect(): ElementFinder {
        return this.userSelect;
    }

    async getUserSelectedOption() {
        return this.userSelect.element(by.css('option:checked')).getText();
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

    async locationSelectLastOption() {
        await this.locationSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async locationSelectOption(option) {
        await this.locationSelect.sendKeys(option);
    }

    getLocationSelect(): ElementFinder {
        return this.locationSelect;
    }

    async getLocationSelectedOption() {
        return this.locationSelect.element(by.css('option:checked')).getText();
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

export class FamilyMemberDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-familyMember-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-familyMember'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
