/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { IhiwLabComponentsPage, IhiwLabDeleteDialog, IhiwLabUpdatePage } from './ihiw-lab.page-object';

const expect = chai.expect;

describe('IhiwLab e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ihiwLabUpdatePage: IhiwLabUpdatePage;
  let ihiwLabComponentsPage: IhiwLabComponentsPage;
  let ihiwLabDeleteDialog: IhiwLabDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load IhiwLabs', async () => {
    await navBarPage.goToEntity('ihiw-lab');
    ihiwLabComponentsPage = new IhiwLabComponentsPage();
    await browser.wait(ec.visibilityOf(ihiwLabComponentsPage.title), 5000);
    expect(await ihiwLabComponentsPage.getTitle()).to.eq('ihiwManagementApp.ihiwLab.home.title');
  });

  it('should load create IhiwLab page', async () => {
    await ihiwLabComponentsPage.clickOnCreateButton();
    ihiwLabUpdatePage = new IhiwLabUpdatePage();
    expect(await ihiwLabUpdatePage.getPageTitle()).to.eq('ihiwManagementApp.ihiwLab.home.createOrEditLabel');
    await ihiwLabUpdatePage.cancel();
  });

  it('should create and save IhiwLabs', async () => {
    const nbButtonsBeforeCreate = await ihiwLabComponentsPage.countDeleteButtons();

    await ihiwLabComponentsPage.clickOnCreateButton();
    await promise.all([
      ihiwLabUpdatePage.setLabCodeInput('labCode'),
      ihiwLabUpdatePage.setTitleInput('title'),
      ihiwLabUpdatePage.setFirstNameInput('firstName'),
      ihiwLabUpdatePage.setLastNameInput('lastName'),
      ihiwLabUpdatePage.setDirectorInput('director'),
      ihiwLabUpdatePage.setDepartmentInput('department'),
      ihiwLabUpdatePage.setInstitutionInput('institution'),
      ihiwLabUpdatePage.setAddress1Input('address1'),
      ihiwLabUpdatePage.setAddress2Input('address2'),
      ihiwLabUpdatePage.setSAddress1Input('sAddress1'),
      ihiwLabUpdatePage.setSAddressInput('sAddress'),
      ihiwLabUpdatePage.setCityInput('city'),
      ihiwLabUpdatePage.setStateInput('state'),
      ihiwLabUpdatePage.setZipInput('zip'),
      ihiwLabUpdatePage.setCountryInput('country'),
      ihiwLabUpdatePage.setPhoneInput('phone'),
      ihiwLabUpdatePage.setFaxInput('fax'),
      ihiwLabUpdatePage.setEmailInput('email'),
      ihiwLabUpdatePage.setUrlInput('url'),
      ihiwLabUpdatePage.setOldLabCodeInput('oldLabCode'),
      ihiwLabUpdatePage.setSNameInput('sName'),
      ihiwLabUpdatePage.setSPhoneInput('sPhone'),
      ihiwLabUpdatePage.setSEmailInput('sEmail'),
      ihiwLabUpdatePage.setDNameInput('dName'),
      ihiwLabUpdatePage.setDEmailInput('dEmail'),
      ihiwLabUpdatePage.setDPhoneInput('dPhone'),
      ihiwLabUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM')
    ]);
    expect(await ihiwLabUpdatePage.getLabCodeInput()).to.eq('labCode', 'Expected LabCode value to be equals to labCode');
    expect(await ihiwLabUpdatePage.getTitleInput()).to.eq('title', 'Expected Title value to be equals to title');
    expect(await ihiwLabUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await ihiwLabUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await ihiwLabUpdatePage.getDirectorInput()).to.eq('director', 'Expected Director value to be equals to director');
    expect(await ihiwLabUpdatePage.getDepartmentInput()).to.eq('department', 'Expected Department value to be equals to department');
    expect(await ihiwLabUpdatePage.getInstitutionInput()).to.eq('institution', 'Expected Institution value to be equals to institution');
    expect(await ihiwLabUpdatePage.getAddress1Input()).to.eq('address1', 'Expected Address1 value to be equals to address1');
    expect(await ihiwLabUpdatePage.getAddress2Input()).to.eq('address2', 'Expected Address2 value to be equals to address2');
    expect(await ihiwLabUpdatePage.getSAddress1Input()).to.eq('sAddress1', 'Expected SAddress1 value to be equals to sAddress1');
    expect(await ihiwLabUpdatePage.getSAddressInput()).to.eq('sAddress', 'Expected SAddress value to be equals to sAddress');
    expect(await ihiwLabUpdatePage.getCityInput()).to.eq('city', 'Expected City value to be equals to city');
    expect(await ihiwLabUpdatePage.getStateInput()).to.eq('state', 'Expected State value to be equals to state');
    expect(await ihiwLabUpdatePage.getZipInput()).to.eq('zip', 'Expected Zip value to be equals to zip');
    expect(await ihiwLabUpdatePage.getCountryInput()).to.eq('country', 'Expected Country value to be equals to country');
    expect(await ihiwLabUpdatePage.getPhoneInput()).to.eq('phone', 'Expected Phone value to be equals to phone');
    expect(await ihiwLabUpdatePage.getFaxInput()).to.eq('fax', 'Expected Fax value to be equals to fax');
    expect(await ihiwLabUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await ihiwLabUpdatePage.getUrlInput()).to.eq('url', 'Expected Url value to be equals to url');
    expect(await ihiwLabUpdatePage.getOldLabCodeInput()).to.eq('oldLabCode', 'Expected OldLabCode value to be equals to oldLabCode');
    expect(await ihiwLabUpdatePage.getSNameInput()).to.eq('sName', 'Expected SName value to be equals to sName');
    expect(await ihiwLabUpdatePage.getSPhoneInput()).to.eq('sPhone', 'Expected SPhone value to be equals to sPhone');
    expect(await ihiwLabUpdatePage.getSEmailInput()).to.eq('sEmail', 'Expected SEmail value to be equals to sEmail');
    expect(await ihiwLabUpdatePage.getDNameInput()).to.eq('dName', 'Expected DName value to be equals to dName');
    expect(await ihiwLabUpdatePage.getDEmailInput()).to.eq('dEmail', 'Expected DEmail value to be equals to dEmail');
    expect(await ihiwLabUpdatePage.getDPhoneInput()).to.eq('dPhone', 'Expected DPhone value to be equals to dPhone');
    expect(await ihiwLabUpdatePage.getCreatedAtInput()).to.contain(
      '2001-01-01T02:30',
      'Expected createdAt value to be equals to 2000-12-31'
    );
    await ihiwLabUpdatePage.save();
    expect(await ihiwLabUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ihiwLabComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last IhiwLab', async () => {
    const nbButtonsBeforeDelete = await ihiwLabComponentsPage.countDeleteButtons();
    await ihiwLabComponentsPage.clickOnLastDeleteButton();

    ihiwLabDeleteDialog = new IhiwLabDeleteDialog();
    expect(await ihiwLabDeleteDialog.getDialogTitle()).to.eq('ihiwManagementApp.ihiwLab.delete.question');
    await ihiwLabDeleteDialog.clickOnConfirmButton();

    expect(await ihiwLabComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
