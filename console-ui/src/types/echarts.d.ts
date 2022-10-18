declare namespace global {
    interface ApiDependencies {
        targetService: string,
        method: string,
        uri: string,
        dependencies: number
    }
}

declare module "echarts/types/dist/shared" {
    export interface GraphNodeItemOption {
        api?: {
            type: string,
            method?: string,
            dependencies: number,
            caller?: string[]
        }
    }
}
