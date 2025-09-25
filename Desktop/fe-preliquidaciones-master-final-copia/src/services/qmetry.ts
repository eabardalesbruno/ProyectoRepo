
export class QMetry {
    private static URL_QMETRY_AUTH_TOKEN_POST = "/rest/api/ui/apikey/generate";
    private static URL_QMETRY_AUTH_TOKEN_GET = "/rest/api/ui/apikey/AUTOMATION/";
    private static QMETRY_URL = "/rest/api/automation/importresult";
    private baseURL:string;

    constructor(url:string){
        this.baseURL=url;
    }

    async sendRequest(method: 'GET' | 'PUT' | 'POST', url: string, headers: Record<string, string>, body?: string): Promise<Response> {
        const options: RequestInit = {
            method: method,
            headers: headers,
            body: body
        };

        try {
            const response = await fetch(url, options);
            console.log(`QMetry ${method} request successful`);
            console.log(`Status Code: ${response.status}`);
            return response;
        } catch (e) {
            console.error(e);
            return null;
        }
    }

    async putUploadResult(url: string, apiKey: string, cucumberJson: string): Promise<Response> {
        const headers = {
            'Content-Type': 'multipart/form-data',
            'apiKey': apiKey
        };
        return this.sendRequest('PUT', url, headers, cucumberJson);
    }

    async getAPIKey(jiraProjectId: string, jiraContextJwt: string): Promise<Response> {
        const url = `${this.baseURL}${QMetry.URL_QMETRY_AUTH_TOKEN_GET}${jiraProjectId}`;
        const headers = {
            'Authorization': `jwt ${jiraContextJwt}`
        };
        return this.sendRequest('GET', url, headers);
    }

    async postAPIKey(jiraProjectId: string, jiraContextJwt: string): Promise<Response> {
        const url = `${this.baseURL}${QMetry.URL_QMETRY_AUTH_TOKEN_POST}${jiraProjectId}`;
        const headers = {
            'Authorization': `jwt ${jiraContextJwt}`
        };
        const body = JSON.stringify({
            "locale": "es_ES",
            "timezone": "America/Bogota",
            "label": "AUTOMATE",
            "apiKeyPurpose": "AUTOMATION",
            "projectId": jiraProjectId
        });
        return this.sendRequest('POST', url, headers, body);
    }

    async generateNewUpload(apiKey:string, body:string ): Promise<Response>{
            const url = `${this.baseURL}${QMetry.QMETRY_URL}`;
            const headers = {
                'Content-Type': 'application/json',
                'apiKey':apiKey
            };
            return this.sendRequest('POST', url, headers, body);
    }
}