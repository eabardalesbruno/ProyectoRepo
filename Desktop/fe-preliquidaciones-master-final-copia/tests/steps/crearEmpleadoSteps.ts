import { Given, Then, When } from "@cucumber/cucumber";
import { expect } from "@playwright/test";
import { LoginPage} from "../pages/loginPage";
import DashboardPage from "../pages/dashboardPage";
import { fixture } from "../../src/hooks/fixture";


let loginPage: LoginPage;
let dashboardPage: DashboardPage;

Given('que estoy en la página de OrangeHRM', async ()=> {
    loginPage = new LoginPage(fixture.page);
    await loginPage.navigateToOrangePage();
})

When('ingreso mis credenciales username {string} y password {string}', async (user, password)=> {
    await loginPage.login(user, password);
})


Then('el inicio de sesión es satisfactorio', async ()=> {
    dashboardPage = new DashboardPage(fixture.page);
    await dashboardPage.loginSuccess();
})