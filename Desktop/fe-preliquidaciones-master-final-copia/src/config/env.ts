import * as dotenv from 'dotenv';


export const getEnv = (environment: string) => {
    dotenv.config(
        {
            override: true,
            path: `resources/.env.${environment}`
        }
    ); 
}