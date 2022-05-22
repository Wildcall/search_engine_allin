import {Api} from "@/api/Api";
import {AxiosResponse} from 'axios'
import qs from "qs";
import {AuthResponse} from "@/models/response/AuthResponse";
import {User} from "@/models/User";

export class AuthService {

    static async login(email: string, password: string): Promise<AxiosResponse<AuthResponse>> {
        console.log("AuthService / login")
        const data = {
            email,
            password
        }
        return Api.getAuth(false).post<AuthResponse>('/user/login', qs.stringify(data))
    }

    static async registration(email: string, password: string): Promise<AxiosResponse<AuthResponse>> {
        console.log("AuthService / registration")
        const data = {
            email,
            password
        }
        return Api.getAuth(false).post<AuthResponse>('/user/registration', data)
    }

    static async getUserInfo(token: string): Promise<AxiosResponse<User>> {
        console.log("AuthService / getUserInfo")
        return Api.getAuth(false).get<User>('/user', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
    }

    static async confirmEmail(token: string): Promise<AxiosResponse> {
        console.log("AuthService / confirmEmail")
        return Api.getAuth(true).get('/user/confirm', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
    }

    static async resendConfirmationEmail(token: string): Promise<AxiosResponse> {
        console.log("AuthService / resendConfirmationEmail")
        return Api.getAuth(false).get('/user/resend', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
    }
}