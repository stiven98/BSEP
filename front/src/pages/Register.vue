<template>
    <div class="q-pa-xl q-mt-xl row justify-center">
      <div class="col-2" >
        <q-form
      @submit="onSubmit"
      class="q-gutter-md"
    >
    <q-btn-toggle
        v-if="admin"
        v-model="role"
        push
        glossy
        toggle-color="primary"
        :options="[
          {label: 'Intermediate', value: 'intermediate'},
          {label: 'Admin', value: 'admin'},
        ]"
      />
      <div v-if="!admin">
       <q-input
        filled
        v-model="firstname"
        label="First name"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"
      />
       <q-input
        filled
        v-model="lastname"
        label="Last name"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"
      />
      </div>
       <q-input v-else
        filled
        v-model="commonName"
        label="Common name"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"
      />
         <q-input
        filled
        v-model="email"
        label="Enter email"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something', isValidEmail]"
      />
          <q-input
        filled
        v-model="pass"
        type="password"
        label="Enter password"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something', passwordValidLength ,passwordValidNumber,passwordValidSpecial,passwordValidUpper]"
      />
          <q-input
        filled
        v-model="pass2"
        type="password"
        label="Confirm password"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something'  && val== pass || 'Please make sure your passwords match ']"
      />
      <div>
        <q-btn label="Register" type="submit" color="primary"/>
      </div>
    </q-form>
      </div>
    </div>
</template>

<script>
export default {
  props: ['admin'],
  name: 'Register',
  data: function () {
    return {
      pass: '',
      pass2: '',
      email: '',
      firstname: '',
      lastname: '',
      commonName: '',
      role: 'user'
    }
  },
  methods: {
    onSubmit () {
      this.$axios.post('https://localhost:8085/api/users/register', {
        email: this.email,
        pass: this.pass,
        pass2: this.pass2,
        commonName: this.firstname + ' ' + this.lastname,
        role: this.role
      })
        .then(response => {
          this.$q.notify({
            type: 'positive',
            message: 'Account created succesfully.'
          })
          if (this.admin) {
            this.$router.push('/adminHome')
            return
          }
          this.$router.push('/')
        })
    },
    isValidEmail (val) {
      const emailPattern = /^(?=[a-zA-Z0-9@._%+-]{6,254}$)[a-zA-Z0-9._%+-]{1,64}@(?:[a-zA-Z0-9-]{1,63}\.){1,8}[a-zA-Z]{2,63}$/
      return emailPattern.test(val) || 'Invalid email'
    },
    passwordValidNumber (val) {
      return /\d/.test(val) || 'Must contain number'
    },
    passwordValidUpper (val) {
      return /[A-Z]/.test(val) || 'Must contain uppercase'
    },
    passwordValidSpecial (val) {
      const format = /[ !@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/
      return format.test(val) || 'Must contain special character '
    },
    passwordValidLength (val) {
      return val.length > 8 || 'Must contain 8 characters '
    }
  }
}
</script>
