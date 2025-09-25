module.exports = {
    default: {
        tags: process.env.npm_config_tags || "",
        formatOptions: {
            snippetInterface: "async-await"
        },
        paths: [
            "tests/features/*.feature"
        ],
        dryRun: false,
        require: [
            "tests/steps/*.ts",
            "src/hooks/hooks.ts"
        ],
        requireModule: [
            "ts-node/register"
        ],
        format: [
            "progress-bar",
            "html:test-results/cucumber_report.html",
            "json:test-results/cucumber_report.json",
            "junit:test-results/cucumber_report.xml",
            "rerun:@rerun.txt"
        ],
        "parallel" : 1
    },
    rerun: {
        formatOptions: {
            snippetInterface: "async-await"
        },
        dryRun: true,
        require: [
            "tests/steps/*.ts",
            "src/hooks/*.ts"
        ],
        requireModule: [
            "ts-node/register"
        ],
        format: [
            "progress-bar",
            "html:test-results/cucumber_report.html",
            "json:test-results/cucumber_report.json",
            "rerun:@rerun.txt"
        ],
        parallel: 2
    }
}
