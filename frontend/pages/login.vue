<template>

  <v-row justify="center" align="center">
    <v-col cols="12" sm="9">
      <v-card>
        <v-row>

          <v-col cols="6" md="6" sm="12">
            <v-card class="transparent elevation-0">
              <v-card-title >
                Mirai Plugin Center
              </v-card-title>
              <v-card-subtitle>
                请登录后进行操作
              </v-card-subtitle>
            </v-card>
          </v-col>

          <v-col cols="6" md="6" sm="12">
            <v-card-title>登录</v-card-title>
            <v-card-text>
              <login-form></login-form>
            </v-card-text>
          </v-col>
        </v-row>

      </v-card>
    </v-col>
  </v-row>

</template>

<script>
import LoginForm from "../components/Auth/LoginForm";
export default {
  name: "login",
  components: {LoginForm},
  layout: "welcome"
}
</script>
