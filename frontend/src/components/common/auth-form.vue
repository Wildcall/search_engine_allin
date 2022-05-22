<template>
  <v-card
      class="bg-grey-lighten-3"
      style="width: 500px"
      elevation="5"
  >
    <v-card-title class="justify-center bg-blue-grey-darken-4">
      <div class="ml-4 text-teal-lighten-5">
        {{ isRegistration ? 'Регистрация' : 'Войти' }}
      </div>
      <v-spacer/>
      <div>
        <v-btn
            color="teal-lighten-5"
            icon="mdi-close"
            variant="text"
            @click="closeForm"
        />
      </div>
    </v-card-title>
    <div class="pa-4">
      <v-card-content>
        <v-form>
          <v-text-field
              v-model="email"
              type="text"
              label="Email"
              :error-messages="(validate.email.$dirty && validate.email.$invalid && email) ? validate.email.$silentErrors[0].$message : ''"
          />
          <v-text-field
              v-model="password"
              label="Пароль"
              :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
              @click:append-inner="showPassword = !showPassword"
              :type="showPassword ? 'text' : 'password'"
              :error-messages="(validate.password.$dirty && validate.password.$invalid && password) ? validate.password.$silentErrors[0].$message : ''"
          />
          <v-text-field
              v-if="isRegistration"
              v-model="confirmPassword"
              label="Подтвердите пароль"
              :append-inner-icon="showConfirmPassword ? 'mdi-eye' : 'mdi-eye-off'"
              @click:append-inner="showConfirmPassword = !showConfirmPassword"
              :type="showConfirmPassword ? 'text' : 'password'"
              :error-messages="(validate.confirmPassword.$dirty && validate.confirmPassword.$invalid && confirmPassword) ? validate.confirmPassword.$silentErrors[0].$message : ''"
          />
        </v-form>
      </v-card-content>

      <v-card-actions>
        <v-row>
          <v-col
              class="d-flex justify-center"
              cols="12">
            <v-btn
                :disabled="validate.$invalid"
                @click="authAction"
            >
              {{ isRegistration ? 'Регистрация' : 'Войти' }}
            </v-btn>
          </v-col>
          <v-divider/>
          <v-col
              class="d-flex justify-center align-center"
              cols="12">
            {{ isRegistration ? 'Уже есть аккаунт?' : 'Еще нет аккаунта?' }}
            <v-btn
                size="small"
                text
                @click="changeForm"
                class="font-weight-bold"
                color="cyan-darken-4"
            >
              {{ isRegistration ? 'Войти' : 'Создать' }}
            </v-btn>
          </v-col>
        </v-row>
      </v-card-actions>
    </div>
  </v-card>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {useAuthStore} from '@/store/AuthStore'
import useVuelidate from '@vuelidate/core'
import {required, email, sameAs, minLength} from '@vuelidate/validators'

export default defineComponent({
  name: "auth-form",

  setup() {
    return {
      validate: useVuelidate(),
      authStore: useAuthStore()
    }
  },

  data() {
    return {
      email: 'nikolay.malygin@gmail.com',
      password: '12345678',
      confirmPassword: '',
      isRegistration: false,
      showPassword: false,
      showConfirmPassword: false,
      closeIcon: true
    }
  },

  validations() {
    const localRules = {
      email: {
        $autoDirty: true,
        required: required,
        email: email
      },
      password: {
        $autoDirty: true,
        required: required,
        minLength: {}
      },
      confirmPassword: {
        $autoDirty: true,
        required: {},
        sameAs: {}
      }
    }

    if (this.isRegistration) {
      localRules.password.minLength = minLength(8)
      localRules.confirmPassword.sameAs = sameAs(this.password)
      localRules.confirmPassword.required = required
    }

    return localRules
  },

  methods: {
    async authAction() {
      if (!await this.validate.$validate()) return
      if (this.authStore.getAuth) return
      if (this.isRegistration)
        this.authStore.registration(this.email, this.password)
            .then(r => {
              if (r)
                this.closeForm()
            })
      else
        this.authStore.login(this.email, this.password)
            .then(r => {
              if (r)
                this.closeForm()
            })
      this.resetForm()
    },

    changeForm() {
      this.isRegistration = !this.isRegistration
      this.resetForm()
    },

    resetForm() {
      this.email = ''
      this.password = ''
      this.confirmPassword = ''
      this.showPassword = false
      this.showConfirmPassword = false
      this.validate.$reset()
    },

    closeForm() {
      this.resetForm()
      this.$emit('close')
    }
  }
})
</script>

<style scoped>

</style>