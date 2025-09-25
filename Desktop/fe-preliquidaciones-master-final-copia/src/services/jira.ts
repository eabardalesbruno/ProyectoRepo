import { el } from "date-fns/locale";


export class Jira {
    private static URL_JIRA_APP: string = "/plugins/servlet/ac/com.infostretch.QmetryTestManager/qtm4j-test-management";
    private jiraUser: string;
    private jiraApiToken: string;
    private jiraContextJwt: string = '';
    private jiraUrl:string;

    constructor(jiraUrl:string, jiraUser: string, jiraApiToken: string) {
        this.jiraUser = jiraUser;
        this.jiraApiToken = jiraApiToken;
        this.jiraUrl= jiraUrl;
    }

    public async getJiraContextJwt(): Promise<string> {
        await this.oauth();
        return this.jiraContextJwt;
    }

    private async oauth(): Promise<void> {
        const userCredentials: string = `${this.jiraUser}:${this.jiraApiToken}`;
        const basicAuth: string = `Basic ${btoa(userCredentials)}`;
        try {
            const response = await fetch(this.jiraUrl+Jira.URL_JIRA_APP, {
                method: 'GET',
                headers: {
                    'Authorization': basicAuth
                }
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const text = await response.text();
            this.jiraContextJwt = this.searchContextJwt(text);
        } catch (error) {
            console.error('Failed to fetch:', error);
        }
    }

    private searchContextJwt(text: string): string | null {
        const pattern: RegExp = /"contextJwt":"(.*?)"/;
        const match = text.match(pattern);
        return match ? match[1] : '';
    }
}