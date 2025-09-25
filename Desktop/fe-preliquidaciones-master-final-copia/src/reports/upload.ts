import { QMetry } from '../services/qmetry';
import { Jira } from '../services/jira';
import { createUploadConfig } from '../util/qmetry-request';
import { format } from 'date-fns';
import { enUS } from 'date-fns/locale';
import { promises as fs } from 'fs';
import { getEnv } from '../config/env';
import minimist from 'minimist';


export class UploadQMetry {
   
    public async uploadQMetry(): Promise<void> {
        
        const args = minimist(process.argv.slice(2));
        const env = args.env;
        
        console.log("Uploading to QMetry");
        console.log( "Ambiente: "+ env)
        
        getEnv(env|| "qa");
        const jira = new Jira(process.env.JIRABASEURL, process.env.JIRAUSEREMAIL, process.env.JIRAAPITOKEN);
        const qmetry = new QMetry(process.env.QMETRYBASEURL);

        const jiraContextJwt = await jira.getJiraContextJwt();
        
        if (!jiraContextJwt) {
            console.error('Error getting Jira Context JWT - APIToken');
            return;
        }

        let apiKey = '';
        const responseGetApiKey = await qmetry.getAPIKey(process.env.JIRAPROJECTID, jiraContextJwt);

        if (responseGetApiKey.status === 200){
            apiKey =(await responseGetApiKey.json()).key;
        }else{
            const responsePostApiKey = await qmetry.postAPIKey(process.env.JIRAPROJECTID, jiraContextJwt);
            if (responsePostApiKey.status === 200) {
                apiKey = (await responsePostApiKey.json()).key;
            }else{
                printErrors(await responsePostApiKey.text(), responsePostApiKey.status, "Error getting ApiKey Token");
                return;
            }
        }

        const {
            USERSTORY,
            COMPONENTS,
            JIRAUSERID,
            PLATAFORMA,
            QUARTER,
            SPRINT,
            SQUAD,
            TESTCYCLEFOLDERID,
            TESTCASEFORLDERID,
            ENVIRONMENT,
            BROWSER
          } = process.env;

        const todayNow:Date = new Date();
        const startDate:string = format(todayNow, "dd/MMM/yyyy HH:mm", { locale: enUS });
        const todayYYYYMMDD:string= format(todayNow, "yyyyMMdd");

        const bodyRequest = createUploadConfig(
            ENVIRONMENT,
            COMPONENTS,
            USERSTORY,
            todayYYYYMMDD,
            JIRAUSERID,
            startDate,
            startDate,
            BROWSER,
            QUARTER,
            SPRINT,
            SQUAD,
            PLATAFORMA,
            TESTCYCLEFOLDERID,
            TESTCASEFORLDERID
        );

        const responseNewUpload = await qmetry.generateNewUpload(apiKey, JSON.stringify(bodyRequest));
        let url:string ='';
        let trackingId:string='';

        if(responseNewUpload.status==200){
            const bodyUpload = (await responseNewUpload.json());
            url = bodyUpload.url;
            trackingId = bodyUpload.trackingId;
        
           const cucumberJson = await readJSONFile();
           const responseUpload = await qmetry.putUploadResult(url,apiKey, cucumberJson);

           if(responseUpload.status==200){
            console.log("trackingId:"+trackingId);
            console.log("Upload Succsess")
           }else{
            printErrors(await responseUpload.text(), responseUpload.status, "Error Automation-uploads");
           }
        }else{
            printErrors(await responseNewUpload.text(), responseNewUpload.status, "Error new Importresult");
        }

    }
}

async function readJSONFile() {
    const path = './test-results/cucumber_report.json';
    const cucumberJson = await fs.readFile(path, 'utf8');
    return cucumberJson;
  }

async function printErrors(body: string, status: number, message: string) {
    console.log("body"+body);
    console.log("status"+ status);
    console.error(message);
  }