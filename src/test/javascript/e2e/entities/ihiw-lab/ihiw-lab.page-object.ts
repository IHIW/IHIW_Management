import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class IhiwLabComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-ihiw-lab div table .btn-danger'));
  title = element.all(by.css('jhi-ihiw-lab div h2#page-heading span')).first();

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

export class IhiwLabUpdatePage {
  pageTitle = element(by.id('jhi-ihiw-lab-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  labCodeInput = element(by.id('field_labCode'));
  titleInput = element(by.id('field_title'));
  firstNameInput = element(by.id('field_firstName'));
  lastNameInput = element(by.id('field_lastName'));
  directorInput = element(by.id('field_director'));
  departmentInput = element(by.id('field_department'));
  institutionInput = element(by.id('field_institution'));
  address1Input = element(by.id('field_address1'));
  address2Input = element(by.id('field_address2'));
  sAddress1Input = element(by.id('field_sAddress1'));
  sAddressInput = element(by.id('field_sAddress'));
  cityInput = element(by.id('field_city'));
  stateInput = element(by.id('field_state'));
  zipInput = element(by.id('field_zip'));
  countryInput = element(by.id('field_country'));
  phoneInput = element(by.id('field_phone'));
  faxInput = element(by.id('field_fax'));
  emailInput = element(by.id('field_email'));
  urlInput = element(by.id('field_url'));
  oldLabCodeInput = element(by.id('field_oldLabCode'));
  sNameInput = element(by.id('field_sName'));
  sPhoneInput = element(by.id('field_sPhone'));
  sEmailInput = element(by.id('field_sEmail'));
  dNameInput = element(by.id('field_dName'));
  dEmailInput = element(by.id('field_dEmail'));
  dPhoneInput = element(by.id('field_dPhone'));
  createdAtInput = element(by.id('field_createdAt'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setLabCodeInput(labCode) {
    await this.labCodeInput.sendKeys(labCode);
  }

  async getLabCodeInput() {
    return await this.labCodeInput.getAttribute('value');
  }

  async setTitleInput(title) {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput() {
    return await this.titleInput.getAttribute('value');
  }

  async setFirstNameInput(firstName) {
    await this.firstNameInput.sendKeys(firstName);
  }

  async getFirstNameInput() {
    return await this.firstNameInput.getAttribute('value');
  }

  async setLastNameInput(lastName) {
    await this.lastNameInput.sendKeys(lastName);
  }

  async getLastNameInput() {
    return await this.lastNameInput.getAttribute('value');
  }

  async setDirectorInput(director) {
    await this.directorInput.sendKeys(director);
  }

  async getDirectorInput() {
    return await this.directorInput.getAttribute('value');
  }

  async setDepartmentInput(department) {
    await this.departmentInput.sendKeys(department);
  }

  async getDepartmentInput() {
    return await this.departmentInput.getAttribute('value');
  }

  async setInstitutionInput(institution) {
    await this.institutionInput.sendKeys(institution);
  }

  async getInstitutionInput() {
    return await this.institutionInput.getAttribute('value');
  }

  async setAddress1Input(address1) {
    await this.address1Input.sendKeys(address1);
  }

  async getAddress1Input() {
    return await this.address1Input.getAttribute('value');
  }

  async setAddress2Input(address2) {
    await this.address2Input.sendKeys(address2);
  }

  async getAddress2Input() {
    return await this.address2Input.getAttribute('value');
  }

  async setSAddress1Input(sAddress1) {
    await this.sAddress1Input.sendKeys(sAddress1);
  }

  async getSAddress1Input() {
    return await this.sAddress1Input.getAttribute('value');
  }

  async setSAddressInput(sAddress) {
    await this.sAddressInput.sendKeys(sAddress);
  }

  async getSAddressInput() {
    return await this.sAddressInput.getAttribute('value');
  }

  async setCityInput(city) {
    await this.cityInput.sendKeys(city);
  }

  async getCityInput() {
    return await this.cityInput.getAttribute('value');
  }

  async setStateInput(state) {
    await this.stateInput.sendKeys(state);
  }

  async getStateInput() {
    return await this.stateInput.getAttribute('value');
  }

  async setZipInput(zip) {
    await this.zipInput.sendKeys(zip);
  }

  async getZipInput() {
    return await this.zipInput.getAttribute('value');
  }

  async setCountryInput(country) {
    await this.countryInput.sendKeys(country);
  }

  async getCountryInput() {
    return await this.countryInput.getAttribute('value');
  }

  async setPhoneInput(phone) {
    await this.phoneInput.sendKeys(phone);
  }

  async getPhoneInput() {
    return await this.phoneInput.getAttribute('value');
  }

  async setFaxInput(fax) {
    await this.faxInput.sendKeys(fax);
  }

  async getFaxInput() {
    return await this.faxInput.getAttribute('value');
  }

  async setEmailInput(email) {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput() {
    return await this.emailInput.getAttribute('value');
  }

  async setUrlInput(url) {
    await this.urlInput.sendKeys(url);
  }

  async getUrlInput() {
    return await this.urlInput.getAttribute('value');
  }

  async setOldLabCodeInput(oldLabCode) {
    await this.oldLabCodeInput.sendKeys(oldLabCode);
  }

  async getOldLabCodeInput() {
    return await this.oldLabCodeInput.getAttribute('value');
  }

  async setSNameInput(sName) {
    await this.sNameInput.sendKeys(sName);
  }

  async getSNameInput() {
    return await this.sNameInput.getAttribute('value');
  }

  async setSPhoneInput(sPhone) {
    await this.sPhoneInput.sendKeys(sPhone);
  }

  async getSPhoneInput() {
    return await this.sPhoneInput.getAttribute('value');
  }

  async setSEmailInput(sEmail) {
    await this.sEmailInput.sendKeys(sEmail);
  }

  async getSEmailInput() {
    return await this.sEmailInput.getAttribute('value');
  }

  async setDNameInput(dName) {
    await this.dNameInput.sendKeys(dName);
  }

  async getDNameInput() {
    return await this.dNameInput.getAttribute('value');
  }

  async setDEmailInput(dEmail) {
    await this.dEmailInput.sendKeys(dEmail);
  }

  async getDEmailInput() {
    return await this.dEmailInput.getAttribute('value');
  }

  async setDPhoneInput(dPhone) {
    await this.dPhoneInput.sendKeys(dPhone);
  }

  async getDPhoneInput() {
    return await this.dPhoneInput.getAttribute('value');
  }

  async setCreatedAtInput(createdAt) {
    await this.createdAtInput.sendKeys(createdAt);
  }

  async getCreatedAtInput() {
    return await this.createdAtInput.getAttribute('value');
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

export class IhiwLabDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-ihiwLab-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-ihiwLab'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
