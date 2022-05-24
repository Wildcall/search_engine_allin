<template>
  <div>
    <div class="text-caption text-medium-emphasis"
         :class="'text-' + textStyle"
    >
      {{ label }}
    </div>
    <v-switch
        v-if="value === true || value === false"
        color="green-darken-1"
        :model-value="value"
        hide-details
    />
    <div
        v-else
        class="text-subtitle-2"
         :class="'text-' + textStyle">
      {{ value ? value : '-' }}
    </div>
  </div>
</template>
<script lang="ts">

import {defineComponent, PropType} from "vue";

export default defineComponent({
  name: 'task-card-line',
  props: {
    textStyle: {} as PropType<string>,
    label: {} as PropType<string>,
    value: {} as PropType<string | boolean>
  }
})
</script>