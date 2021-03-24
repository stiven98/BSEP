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
      this.$axios.post('http://localhost:8085/api/users/login', {
        username: this.username,
        password: this.pass
      })
        .then(response => {
          localStorage.setItem('user', response.data.username)
          localStorage.setItem('role', response.data.role)
          if (response.data.role === 'user') {
            this.$router.push('userHome')
          }
          if (response.data.role === 'admin') {
            this.$router.push('adminHome')
          }
        })
        .catch(err => {
          console.log(err)
        })
    }
  }
}
</script>
