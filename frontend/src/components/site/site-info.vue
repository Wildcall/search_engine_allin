<template>
  <v-hover
      v-slot="{ isHovering, props }"
  >
    <v-card
        class="pa-4 bg-grey-lighten-3"
        :elevation="isHovering ? 20 : 5"
        v-bind="props"
        @click="this.$emit('click-row', site.id)"
    >
      <v-row>

        <v-col cols="1"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ site.id }}</h4>
        </v-col>

        <v-col cols="2"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ site.name }}</h4>
        </v-col>

        <v-col cols="3"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ site.path }}</h4>
        </v-col>

        <v-col cols="2"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ site.lastStatusTime }}</h4>
        </v-col>

        <v-col cols="2"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ site.status }}</h4>
        </v-col>

        <v-col cols="2"
               class="d-flex align-center"
        >
          <v-btn
              size="x-small"
              text
              variant="plain"
              icon="mdi-magnify"
              @click="this.$emit('click-search', site.id)"
              class="mr-4"
          />
          <v-btn
              size="x-small"
              text
              variant="plain"
              icon="mdi-close"
              @click="this.$emit('click-delete', site.id)"
          />
        </v-col>
      </v-row>
    </v-card>
  </v-hover>
</template>

<script lang="ts">
import {defineComponent, PropType} from "vue";
import {SiteResponse} from "@/models/response/SiteResponse";

export default defineComponent({
  name: "site-info",
  emits: [
    "click-row",
    "click-search",
    "click-delete"
  ],

  props: {
    site: Object as PropType<SiteResponse>
  },
})
</script>

<style scoped>

</style>