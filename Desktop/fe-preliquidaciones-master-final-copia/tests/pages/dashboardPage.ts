import { expect, Page } from "@playwright/test";

export default class DashboardPage{
    private readonly page: Page;

    constructor(page: Page){
        this.page=page;
    }
    
    private Elements = {
        dashboardTitle: "//header/div[1]/div[1]/span[1]/h6[1]"
    }

    async loginSuccess(){
        await this.page.waitForSelector(this.Elements.dashboardTitle);
        await expect(this.page.locator(this.Elements.dashboardTitle)).toBeVisible();
    }
}