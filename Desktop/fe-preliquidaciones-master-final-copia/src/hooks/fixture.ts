import { BrowserContext, Page } from "@playwright/test";
import { Logger } from "winston";

export const fixture = {
    page: undefined as Page | undefined,
    logger: undefined as Logger | undefined,
    setPage: (page: Page) => {
        fixture.page = page;
    }

};