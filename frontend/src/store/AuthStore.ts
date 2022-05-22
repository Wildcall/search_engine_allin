import {User} from "@/models/User";
import {defineStore} from "pinia";
import {AuthService} from "@/services/AuthService";
import {StorageService} from "@/services/StorageService";
import {AxiosResponse} from 'axios'
import {useUserStore} from "@/store/UserState";
import {useErrorStore} from "@/store/ErrorStore";

export interface AuthState {
    auth: boolean
    loading: boolean
}

export const useAuthStore = defineStore({
    id: 'auth',

    state: () => ({
        auth: false,
        loading: false,
    } as AuthState),

    getters: {
        getAuth(state: AuthState): boolean {
            return state.auth
        },

        getLoading(state: AuthState): boolean {
            return state.loading
        },
    },

    actions: {
        async login(email: string, password: string) {
            console.log("AuthStore / login")
            const errorStore = useErrorStore()
            this.loading = true
            return AuthService.login(email, password)
                .then(response => this.setupData(response))
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async registration(email: string, password: string) {
            console.log("AuthStore / registration")
            const errorStore = useErrorStore()
            this.loading = true
            return AuthService.registration(email, password)
                .then(response => this.setupData(response))
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async getUserInfo() {
            console.log("AuthStore / getInfo")
            const errorStore = useErrorStore()
            const token = StorageService.getLocalAccessToken()
            if (token) {
                this.loading = true
                return AuthService.getUserInfo(token)
                    .then(response => {
                        if (response?.data) {
                            const userStore = useUserStore()
                            userStore.save({
                                id: response.data.id,
                                email: response.data.email,
                                roles: response.data.roles
                            } as User)
                            this.auth = true
                            this.loading = false
                            return true
                        }
                    })
                    .catch(error => errorStore.save(error))
                    .finally(() => this.loading = false)
            }
            return Promise.resolve(false)
        },

        async confirmEmail(token: string) {
            console.log("AuthStore / confirmEmail")
            const errorStore = useErrorStore()
            this.loading = true
            AuthService.confirmEmail(token)
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async resendConfirmationEmail() {
            const token = StorageService.getLocalAccessToken()
            const errorStore = useErrorStore()
            if (token) {
                this.loading = true
                return AuthService.resendConfirmationEmail(token)
                    .catch(error => errorStore.save(error))
                    .finally(() => this.loading = false)
            }
        },

        logout() {
            console.log("AuthStore / logout")
            StorageService.removeAuthResponse()
            this.auth = false
        },

        setupData(response: AxiosResponse) {
            const userStore = useUserStore()
            if (response?.data) {
                StorageService.setAuthResponse(response.data)
                userStore.save({
                    id: response.data.id,
                    email: response.data.email,
                    roles: response.data.roles
                } as User)
                this.auth = true
                this.loading = false
                return true
            }
            return false
        },
    }
})

