import {AuthResponse} from "@/models/response/AuthResponse";

export class StorageService {

    static getLocalRefreshToken(): string | null {
        const item: string | null = localStorage.getItem("user");
        if (item) {
            const user: AuthResponse = JSON.parse(item);
            return user?.refreshToken;
        }
        return null
    }

    static getLocalAccessToken(): string | null {
        const item: string | null = localStorage.getItem("user");
        if (item) {
            const user: AuthResponse = JSON.parse(item);
            return user?.accessToken;
        }
        return null
    }

    static updateLocalAccessToken(token: string) {
        const item: string | null = localStorage.getItem("user");
        if (item) {
            const user: AuthResponse = JSON.parse(item);
            user.accessToken = token
            localStorage.setItem("user", JSON.stringify(user))
        }
    }

    static setAuthResponse(user: AuthResponse) {
        const item: string = JSON.stringify(user)
        localStorage.setItem("user", item)
    }

    static removeAuthResponse() {
        localStorage.removeItem("user")
    }
}
