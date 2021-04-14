<template>
  <v-dialog
    v-model="dialog"
    fullscreen
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        color="primary"
        dark
        v-bind="attrs"
        v-on="on"
        small class="transparent elevation-0"
        @click="$emit('click');dialog = true"
      >登录</v-btn>
    </template>
    <v-card height="70%">
      <v-toolbar
        dark
        color="primary"
      >
        <v-btn
          icon
          dark
          @click="dialog = false"
        >
          <v-icon>mdi-close</v-icon>
        </v-btn>
        <v-toolbar-title>用户验证-登陆</v-toolbar-title>
        <v-spacer></v-spacer>
      </v-toolbar>
      <v-row justify="center" align="center" style="height: 80%">
        <v-col cols="12" sm="9">
        <v-row>
          <v-col cols="12" md="6" sm="12">
            <v-card class="transparent elevation-0">
              <v-card-title >
                Mirai Plugin Center
              </v-card-title>
              <v-card-subtitle>
                请登录后进行操作
              </v-card-subtitle>
            </v-card>
          </v-col>

          <v-col cols="12" md="6" sm="12">
            <v-card-title>登录</v-card-title>
            <v-card-text>
              <login-form></login-form>
            </v-card-text>
          </v-col>
        </v-row>

        </v-col>
      </v-row>
    </v-card>

  </v-dialog>
</template>

<script>
import NeedLogin from "./NeedLogin";
import LoginForm from "../../Auth/LoginForm";
export default {
  name: "AuthDialog",
  components: {LoginForm, NeedLogin},
  data: () => ({
    dialog: false,
    register: false,
  })
}
</script>

<style scoped>

</style>
