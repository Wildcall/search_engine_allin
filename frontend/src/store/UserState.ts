import {User} from "@/models/User";
import {defineStore} from "pinia";
import {UserRole} from "@/models/UserRole";

export interface UserState {
    user: User
}

export const useUserStore = defineStore({
    id: 'user',

    state: () => ({
        user: {} as User,
    } as UserState),

    getters: {
        getId(state: UserState): number {
            return state.user?.id
        },

        getEmail(state: UserState): string {
            return state.user?.email
        },

        userHasRole(state: UserState): (role: UserRole) => boolean {
            return (role: UserRole) => {
                return state.user?.roles?.includes(role)
            }
        },
    },

    actions: {
        save(user: User) {
            console.log("UserStore / save")
            this.user = user
        },

        remove() {
            console.log("UserStore / remove")
            this.user = {} as User
        }
    }
})