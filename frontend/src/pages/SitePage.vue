<template>
  <v-container>
    <v-row>

      <v-col cols="12">
        <h1 class="font-weight-bold text-blue-grey-darken-4">
          Sites
        </h1>
      </v-col>

      <v-col cols="3">
        <info-card
            @add="newSiteForm = true"
            type="TOTAL"
            button
            :data="this.siteStore.getSites.length"
        />
      </v-col>

      <v-col cols="3">
        <info-card
            type="COMPLETE"
            :data="siteStore.getCountSitesWithAllCompleteTasks"
        />
      </v-col>

      <v-col cols="3">
        <info-card
            type="PROGRESSING"
            :data="siteStore.getProcessingSitesCount"
        />
      </v-col>


      <v-col cols="3">
        <info-card
            type="INTERRUPT"
            :data="NaN"
        />
      </v-col>

      <v-col cols="12"
             v-if="siteStore.getSites.length===0"
      >
        <v-card
            class="pa-4 bg-grey-lighten-3"
        >
          <v-card-title>
            Сайтов пока что нет
          </v-card-title>
        </v-card>
      </v-col>

      <v-col cols="12"
             v-else
             v-for="site in siteStore.getSites"
             :key="site.id"
      >
        <site-info
            :site="site"
        />
      </v-col>

    </v-row>

    <v-dialog
        v-model="newSiteForm"
        persistent
    >
      <site-new
          @close="newSiteForm=false"
      />
    </v-dialog>

  </v-container>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {useSiteStore} from "@/store/SiteStore";
import SiteNew from "@/components/site/site-new.vue"
import SiteInfo from "@/components/site/site-info.vue";
import InfoCard from "@/components/common/info-card.vue";

export default defineComponent({
  name: "SitePage",

  components: {InfoCard, SiteNew, SiteInfo},

  setup() {
    return {
      siteStore: useSiteStore()
    }
  },

  data() {
    return {
      newSiteForm: false
    }
  },

  mounted() {
    this.siteStore.findAll()
  }
})
</script>

<style scoped>

</style>