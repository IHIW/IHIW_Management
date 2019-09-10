/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { UploadComponentsPage, UploadDeleteDialog, UploadUpdatePage } from './upload.page-object';

const expect = chai.expect;

describe('Upload e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let uploadUpdatePage: UploadUpdatePage;
  let uploadComponentsPage: UploadComponentsPage;
  let uploadDeleteDialog: UploadDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Uploads', async () => {
    await navBarPage.goToEntity('upload');
    uploadComponentsPage = new UploadComponentsPage();
    await browser.wait(ec.visibilityOf(uploadComponentsPage.title), 5000);
    expect(await uploadComponentsPage.getTitle()).to.eq('ihiwManagementApp.upload.home.title');
  });

  it('should load create Upload page', async () => {
    await uploadComponentsPage.clickOnCreateButton();
    uploadUpdatePage = new UploadUpdatePage();
    expect(await uploadUpdatePage.getPageTitle()).to.eq('ihiwManagementApp.upload.home.createOrEditLabel');
    await uploadUpdatePage.cancel();
  });

  it('should create and save Uploads', async () => {
    const nbButtonsBeforeCreate = await uploadComponentsPage.countDeleteButtons();

    await uploadComponentsPage.clickOnCreateButton();
    await promise.all([
      uploadUpdatePage.typeSelectLastOption(),
      uploadUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      uploadUpdatePage.setModifiedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      uploadUpdatePage.setFileNameInput('fileName'),
      uploadUpdatePage.createdBySelectLastOption()
    ]);
    expect(await uploadUpdatePage.getCreatedAtInput()).to.contain(
      '2001-01-01T02:30',
      'Expected createdAt value to be equals to 2000-12-31'
    );
    expect(await uploadUpdatePage.getModifiedAtInput()).to.contain(
      '2001-01-01T02:30',
      'Expected modifiedAt value to be equals to 2000-12-31'
    );
    expect(await uploadUpdatePage.getFileNameInput()).to.eq('fileName', 'Expected FileName value to be equals to fileName');
    const selectedValid = uploadUpdatePage.getValidInput();
    if (await selectedValid.isSelected()) {
      await uploadUpdatePage.getValidInput().click();
      expect(await uploadUpdatePage.getValidInput().isSelected(), 'Expected valid not to be selected').to.be.false;
    } else {
      await uploadUpdatePage.getValidInput().click();
      expect(await uploadUpdatePage.getValidInput().isSelected(), 'Expected valid to be selected').to.be.true;
    }
    const selectedEnabled = uploadUpdatePage.getEnabledInput();
    if (await selectedEnabled.isSelected()) {
      await uploadUpdatePage.getEnabledInput().click();
      expect(await uploadUpdatePage.getEnabledInput().isSelected(), 'Expected enabled not to be selected').to.be.false;
    } else {
      await uploadUpdatePage.getEnabledInput().click();
      expect(await uploadUpdatePage.getEnabledInput().isSelected(), 'Expected enabled to be selected').to.be.true;
    }
    await uploadUpdatePage.save();
    expect(await uploadUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await uploadComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Upload', async () => {
    const nbButtonsBeforeDelete = await uploadComponentsPage.countDeleteButtons();
    await uploadComponentsPage.clickOnLastDeleteButton();

    uploadDeleteDialog = new UploadDeleteDialog();
    expect(await uploadDeleteDialog.getDialogTitle()).to.eq('ihiwManagementApp.upload.delete.question');
    await uploadDeleteDialog.clickOnConfirmButton();

    expect(await uploadComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
