import {ResourceSetting} from "@/models/setting/ResourceSetting";
import {ResourceType} from "@/models/ResourceType";

export class CrawlerSetting extends ResourceSetting {

    private _name: string
    private _referrer: string
    private _userAgent: string
    private _delayInMs: number
    private _reconnect: number
    private _timeOutInMs: number

    constructor(name: string,
                referrer: string,
                userAgent: string,
                delayInMs: number,
                reconnect: number,
                timeOutInMs: number) {
        super();
        this._name = name;
        this._referrer = referrer;
        this._userAgent = userAgent;
        this._delayInMs = delayInMs;
        this._reconnect = reconnect;
        this._timeOutInMs = timeOutInMs;
    }

    get name(): string {
        return this._name;
    }

    set name(value: string) {
        this._name = value;
    }

    get referrer(): string {
        return this._referrer;
    }

    set referrer(value: string) {
        this._referrer = value;
    }

    get userAgent(): string {
        return this._userAgent;
    }

    set userAgent(value: string) {
        this._userAgent = value;
    }

    get delayInMs(): number {
        return this._delayInMs;
    }

    set delayInMs(value: number) {
        this._delayInMs = value;
    }

    get reconnect(): number {
        return this._reconnect;
    }

    set reconnect(value: number) {
        this._reconnect = value;
    }

    get timeOutInMs(): number {
        return this._timeOutInMs;
    }

    set timeOutInMs(value: number) {
        this._timeOutInMs = value;
    }

    getType(): ResourceType {
        return ResourceType.CRAWLER;
    }
}