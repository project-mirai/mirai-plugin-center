<template>
  <v-list>
    <v-list-item>
      <v-list-item-content>
        <v-list-item-title class="title">
          未登录
        </v-list-item-title>
        <v-list-item-subtitle>登陆后访问更多功能</v-list-item-subtitle>
      </v-list-item-content>

      <v-list-item-action>
        <!--
        <authorization-dialog @click="$emit('click')"></authorization-dialog>
        -->
        <v-btn
          color="primary"
          dark
          small class="transparent elevation-0"
          @click="$emit('click');"
        >登录</v-btn>
      </v-list-item-action>
    </v-list-item>
  </v-list>
</template>

<script>
export default {
  name: "NeedLogin"
}
</script>

<style scoped>

</style>
