import { Page } from "@playwright/test";
export class LoginPage {
    private readonly page: Page;

    constructor(page: Page){
        this.page=page;
    }

    private Elements = {
        usernameInput: "//input[@name='username']",
        passwordInput: "//input[@name='password']",
        loginBtn: "//body/div[@id='app']/div[1]/div[1]/div[1]/div[1]/div[2]/div[2]/form[1]/div[3]/button[1]"
    }

    async navigateToOrangePage() {
        await this.page.goto("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        await this.page.waitForSelector(this.Elements.usernameInput);
    }

    async login(user: string, password: string) {
        await this.page.waitForSelector(this.Elements.usernameInput);
        await this.page.type(this.Elements.usernameInput, user);
        await this.page.type(this.Elements.passwordInput, password)
        await this.page.click(this.Elements.loginBtn);
    }
}