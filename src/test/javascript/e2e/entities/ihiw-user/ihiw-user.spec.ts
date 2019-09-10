/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { IhiwUserComponentsPage, IhiwUserDeleteDialog, IhiwUserUpdatePage } from './ihiw-user.page-object';

const expect = chai.expect;

describe('IhiwUser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ihiwUserUpdatePage: IhiwUserUpdatePage;
  let ihiwUserComponentsPage: IhiwUserComponentsPage;
  let ihiwUserDeleteDialog: IhiwUserDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load IhiwUsers', async () => {
    await navBarPage.goToEntity('ihiw-user');
    ihiwUserComponentsPage = new IhiwUserComponentsPage();
    await browser.wait(ec.visibilityOf(ihiwUserComponentsPage.title), 5000);
    expect(await ihiwUserComponentsPage.getTitle()).to.eq('ihiwManagementApp.ihiwUser.home.title');
  });

  it('should load create IhiwUser page', async () => {
    await ihiwUserComponentsPage.clickOnCreateButton();
    ihiwUserUpdatePage = new IhiwUserUpdatePage();
    expect(await ihiwUserUpdatePage.getPageTitle()).to.eq('ihiwManagementApp.ihiwUser.home.createOrEditLabel');
    await ihiwUserUpdatePage.cancel();
  });

  it('should create and save IhiwUsers', async () => {
    const nbButtonsBeforeCreate = await ihiwUserComponentsPage.countDeleteButtons();

    await ihiwUserComponentsPage.clickOnCreateButton();
    await promise.all([
      ihiwUserUpdatePage.setPhoneInput('phone'),
      ihiwUserUpdatePage.userSelectLastOption(),
      ihiwUserUpdatePage.labSelectLastOption()
    ]);
    expect(await ihiwUserUpdatePage.getPhoneInput()).to.eq('phone', 'Expected Phone value to be equals to phone');
    await ihiwUserUpdatePage.save();
    expect(await ihiwUserUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ihiwUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last IhiwUser', async () => {
    const nbButtonsBeforeDelete = await ihiwUserComponentsPage.countDeleteButtons();
    await ihiwUserComponentsPage.clickOnLastDeleteButton();

    ihiwUserDeleteDialog = new IhiwUserDeleteDialog();
    expect(await ihiwUserDeleteDialog.getDialogTitle()).to.eq('ihiwManagementApp.ihiwUser.delete.question');
    await ihiwUserDeleteDialog.clickOnConfirmButton();

    expect(await ihiwUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
