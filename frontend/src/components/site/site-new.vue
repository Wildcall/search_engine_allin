<template>
  <v-card
      class="bg-grey-lighten-3"
      style="width: 500px"
      elevation="5"
  >
    <v-card-title class="justify-center bg-blue-grey-darken-4">
      <div class="ml-4 text-teal-lighten-5">
        Добавить сайт
      </div>
      <v-spacer/>
      <div>
        <v-btn
            color="teal-lighten-5"
            icon="mdi-close"
            variant="text"
            @click="this.close"
        />
      </div>
    </v-card-title>
    <div class="pa-4">
      <v-card-content>
        <v-form>
          <v-text-field
              v-model="site.name"
              type="text"
              label="Название"
              :error-messages="(v.site.name.$dirty && v.site.name.$invalid && site.name) ? v.site.name.$silentErrors[0].$message : ''"
          />
          <v-text-field
              v-model="site.path"
              type="text"
              label="Адрес"
              :error-messages="(v.site.path.$dirty && v.site.path.$invalid && site.path) ? v.site.path.$silentErrors[0].$message : ''"
          />
        </v-form>
      </v-card-content>

      <v-divider class="mb-4"/>

      <v-card-actions class="d-flex justify-center">
        <v-btn
            :disabled="v.$invalid"
            text
            class="font-weight-bold"
            @click="this.save"
        >
          Сохранить
        </v-btn>
      </v-card-actions>
    </div>
  </v-card>
</template>

<script>
import {defineComponent} from "vue";
import useVuelidate from "@vuelidate/core";
import {url, minLength, maxLength, required} from "@vuelidate/validators";
import {useSiteStore} from "@/store/SiteStore";

export default defineComponent({
  name: "site-new",

  setup() {
    return {
      v: useVuelidate(),
      siteStore: useSiteStore()
    }
  },

  data() {
    return {
      site: {
        name: '',
        path: ''
      }
    }
  },

  validations() {
    return {
      site: {
        name: {
          $autoDirty: true,
          required: required,
          minLength: minLength(3),
          maxLength: maxLength(32)
        },
        path: {
          $autoDirty: true,
          required: required,
          url: url
        },
      }
    }
  },

  methods: {
    async save() {
      if (!await this.v.$validate()) return
      await this.siteStore.add(this.site)
      this.close()
    },

    close() {
      this.reset()
      this.$emit('close')
    },

    reset() {
      this.site.name = ''
      this.site.path = ''
    }
  }
})
</script>

<style scoped>

</style>