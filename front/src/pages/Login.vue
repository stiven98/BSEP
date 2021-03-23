<template>

    <div style="max-width:400px">
        <q-form
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input
        filled
        v-model="username"
        label="Your username *"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"
      />

      <q-input
        filled
        v-model="pass"
        label="Your password"
        lazy-rules
       :rules="[ val => val && val.length > 0 || 'Please type something']"
      />

      <div>
        <q-btn label="Login" type="submit" color="primary"/>
      </div>
    </q-form>

  </div>
</template>

<script>
export default {
  name: 'PageIndex',
  data: function () {
    return {
      username: '',
      pass: ''
    }
  },
  methods: {
    onSubmit () {
      if (this.username === 'admin' && this.pass === 'admin') {
        this.$router.push('/adminHome')
        return
      }
      if (this.username === 'user' && this.pass === 'user') {
        this.$router.push('/userHome')
        return
      }
      alert('Wrong username or password')
    }
  }
}
</script>
