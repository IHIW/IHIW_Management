import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class ProjectComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-project div table .btn-danger'));
  title = element.all(by.css('jhi-project div h2#page-heading span')).first();

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

export class ProjectUpdatePage {
  pageTitle = element(by.id('jhi-project-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
  createdAtInput = element(by.id('field_createdAt'));
  modifiedAtInput = element(by.id('field_modifiedAt'));
  createdBySelect = element(by.id('field_createdBy'));
  modifiedBySelect = element(by.id('field_modifiedBy'));
  labSelect = element(by.id('field_lab'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return await this.nameInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return await this.descriptionInput.getAttribute('value');
  }

  async setCreatedAtInput(createdAt) {
    await this.createdAtInput.sendKeys(createdAt);
  }

  async getCreatedAtInput() {
    return await this.createdAtInput.getAttribute('value');
  }

  async setModifiedAtInput(modifiedAt) {
    await this.modifiedAtInput.sendKeys(modifiedAt);
  }

  async getModifiedAtInput() {
    return await this.modifiedAtInput.getAttribute('value');
  }

  async createdBySelectLastOption(timeout?: number) {
    await this.createdBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async createdBySelectOption(option) {
    await this.createdBySelect.sendKeys(option);
  }

  getCreatedBySelect(): ElementFinder {
    return this.createdBySelect;
  }

  async getCreatedBySelectedOption() {
    return await this.createdBySelect.element(by.css('option:checked')).getText();
  }

  async modifiedBySelectLastOption(timeout?: number) {
    await this.modifiedBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async modifiedBySelectOption(option) {
    await this.modifiedBySelect.sendKeys(option);
  }

  getModifiedBySelect(): ElementFinder {
    return this.modifiedBySelect;
  }

  async getModifiedBySelectedOption() {
    return await this.modifiedBySelect.element(by.css('option:checked')).getText();
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

export class ProjectDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-project-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-project'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
