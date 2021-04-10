<template>

  <v-row justify="center" align="center">
    <v-col cols="12" sm="9">
      <v-card class="d-flex">
        <v-col align-self="center" justify="center">
    <p>dsadas</p>
        </v-col>
        <v-col >
          <v-card-title>登录</v-card-title>
          <v-card-text>
            <v-form
              ref="form"
              v-model="valid"
              lazy-validation>
              <v-text-field
                v-model="email"
                label="邮箱"
                requiredp
              ></v-text-field>

              <v-text-field
                type="password"
                label="密码"
                reqired/>

              <v-btn
                :disabled="!valid"
                color="success"
                class="mr-4">
                登录
              </v-btn>
            </v-form>

          </v-card-text>


        </v-col>

      </v-card>
    </v-col>
  </v-row>

</template>

<script>
export default {
  name: "login",
  layout: "welcome"
}
</script>
