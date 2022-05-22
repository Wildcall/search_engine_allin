import {ResourceSetting} from "@/models/setting/ResourceSetting";
import {ResourceType} from "@/models/ResourceType";

export class SearcherSetting extends ResourceSetting {

    private name: string

    constructor(name: string) {
        super();
        this.name = name;
    }

    getType(): ResourceType {
        return ResourceType.SEARCHER;
    }
}