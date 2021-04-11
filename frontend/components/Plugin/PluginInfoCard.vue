<template>
  <v-card>
    <v-card-text>
      <p class="display-1 text--primary">
        {{info.name}}
      </p>
      <p>作者：{{info.owner.nick}} {{info.owner.email}}</p>
      <div class="text--primary">
        {{info.info}}
      </div>
    </v-card-text>
    <v-card-actions>
      <v-btn
        text
        color="deep-purple accent-4"
      >
        下载
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import {generateFakePluginInfo} from "../../utils/plugin";

export default {
  name: "PluginInfoCard",
  props: {
    info: {
      type: Object,
      default: function () {
        return generateFakePluginInfo()
      }
    }
  }
}
</script>

<style scoped>

</style>
