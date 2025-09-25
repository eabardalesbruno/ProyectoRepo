import { Given, Then, When } from "@cucumber/cucumber";
import { fixture } from "../../src/hooks/fixture";

import { LoginPortalPage } from "../pages/loginPortalPage";
import { expect } from "@playwright/test";


let loginPortalPage: LoginPortalPage;




Given('Web was opened', async () => { 
    loginPortalPage = new LoginPortalPage(fixture.page);
    await loginPortalPage.navigateToPortalPage();
});

When('Enter credentials of user {string} with password {string}', async (user: string, password: string) => {
    await loginPortalPage.seleccionarSomosCorredores();
    await loginPortalPage.signInSuccess(user, password);
});


