import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class IhiwUserComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-ihiw-user div table .btn-danger'));
  title = element.all(by.css('jhi-ihiw-user div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class IhiwUserUpdatePage {
  pageTitle = element(by.id('jhi-ihiw-user-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  phoneInput = element(by.id('field_phone'));
  userSelect = element(by.id('field_user'));
  labSelect = element(by.id('field_lab'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setPhoneInput(phone) {
    await this.phoneInput.sendKeys(phone);
  }

  async getPhoneInput() {
    return await this.phoneInput.getAttribute('value');
  }

  async userSelectLastOption(timeout?: number) {
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
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async labSelectLastOption(timeout?: number) {
    await this.labSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async labSelectOption(option) {
    await this.labSelect.sendKeys(option);
  }

  getLabSelect(): ElementFinder {
    return this.labSelect;
  }

  async getLabSelectedOption() {
    return await this.labSelect.element(by.css('option:checked')).getText();
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class IhiwUserDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-ihiwUser-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-ihiwUser'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
