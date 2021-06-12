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
    <q-dialog v-model="verifyCode" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-input v-model="code" label="Enter your code:"/>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancel" color="primary" v-close-popup />
          <q-btn v-on:click="verifyCodePost" flat label="Verify" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
      <q-input
        filled
        v-model="username"
        label="Your email "
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"
      />

      <q-input
        filled
        v-model="pass"
        label="Your password"
        lazy-rules
        type="password"
       :rules="[ val => val && val.length > 0 || 'Please type something']"
      />
      <q-btn  v-on:click="forgotPass=true" class="text-primary" flat>Forgot password? </q-btn>
      <div class="row items-center">
        <div> Don't have account? </div>
        <q-btn  v-on:click="$router.push('register')" class="text-primary" flat>Register</q-btn>
      </div>
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
      forgotEmail: '',
      verifyCode: false,
      code: ''
    }
  },
  beforeMount () {
    localStorage.setItem('user', '')
    localStorage.setItem('token', '')
    localStorage.setItem('role', '')
  },
  methods: {
    onSubmit () {
      this.$axios.post('https://localhost:8085/api/users/login', {
        username: this.username,
        password: this.pass
      })
        .then(response => {
          localStorage.setItem('user', response.data.username)
          localStorage.setItem('token', response.data.token)
          if (response.data.authorityList.includes('ROLE_USER')) {
            localStorage.setItem('role', 'ROLE_USER')
            // this.$router.push('userHome')
          }
          if (response.data.authorityList.includes('ROLE_ADMIN')) {
            localStorage.setItem('role', 'ROLE_ADMIN')
            // this.$router.push('adminHome')
          }
          this.verifyCode = true
        })
        .catch(err => {
          console.log(err)
        })
    },
    sendRequest () {
      this.$axios.post('https://localhost:8085/api/users/forgotPassword', this.forgotEmail, { headers: { 'Content-Type': 'text/plain' } })
        .then(res => {
          alert(res.data)
        })
    },
    specialCharacter (val) {
      const format = /^((?![<>?=+-;:'/,]).)*$/
      return format.test(val) || 'Must not contain special characters!'
    },
    verifyCodePost () {
      var data = {
        username: this.username,
        code: this.code
      }
      this.$axios.post('https://localhost:8085/api/users/verifyCode', data)
        .then(res => {
          if (localStorage.getItem('role') === 'ROLE_USER') { this.$router.push('userHome') }
          if (localStorage.getItem('role') === 'ROLE_ADMIN') { this.$router.push('adminHome') }
        })
        .catch(err => {
          console.log(err)
          alert('invalid verification code')
        })
    }
  }
}
</script>
