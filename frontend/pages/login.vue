<template>

  <v-row justify="center" align="center">
    <v-col cols="12" sm="9">
      <v-card class="d-flex">
        <v-col align-self="center" justify="center">
           <p>dsadas</p>
        </v-col>
        <v-col>
          <v-card-title>登录</v-card-title>
          <v-card-text>
            <login-form></login-form>
          </v-card-text>


        </v-col>

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
