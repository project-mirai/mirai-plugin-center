<template>
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
      :disabled="valid"
      color="success"
      class="mr-4">
      登录
    </v-btn>
  </v-form>
</template>

<script>
export default {
  name: "LoginForm",
  data() {
    return {
      valid: false //表示当前是否可用
    }
  }
}
</script>

<style scoped>

</style>
