<template>
    <div class="q-pa-xl q-mt-xl row justify-center">
      <div class="col-2" >
        <q-form
      @submit="onSubmit"
      class="q-gutter-md"
    >
    <q-dialog v-model="forgotPass" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-input v-model="forgotEmail" label="Enter your email:"/>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancel" color="primary" v-close-popup />
          <q-btn v-on:click="sendRequest" flat label="Send recovery mail" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
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
      <q-btn  v-on:click="forgotPass=true" class="text-primary" flat>Forgot password? </q-btn>
      <div>
        <q-btn label="Login" type="submit" color="primary"/>
      </div>
    </q-form>
      </div>
    </div>
</template>

<script>
export default {
  name: 'PageIndex',
  data: function () {
    return {
      username: '',
      pass: '',
      forgotPass: false,
      forgotEmail: ''
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
          localStorage.setItem('role', response.data.authorityList[0].authority)
          if (response.data.authorityList[0].authority === 'user') {
            this.$router.push('userHome')
          }
          if (response.data.authorityList[0].authority === 'admin') {
            this.$router.push('adminHome')
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    sendRequest () {
      this.$axios.post('http://localhost:8085/api/users/forgotPassword', this.forgotEmail, { headers: { 'Content-Type': 'text/plain' } })
        .then(res => {
          alert(res.data)
        })
    }
  }
}
</script>
