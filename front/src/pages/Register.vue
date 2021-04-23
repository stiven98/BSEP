<template>
    <div class="q-pa-xl q-mt-xl row justify-center">
      <div class="col-2" >
        <q-form
      @submit="onSubmit"
      class="q-gutter-md"
    >
    <q-btn-toggle
        v-model="nameSelect"
        push
        glossy
        toggle-color="primary"
        :options="[
          {label: 'Intermediate', value: 'intermediate'},
          {label: 'End entity', value: 'endEntity'},
        ]"
      />
      <div v-if="nameSelect ==='endEntity'">
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
        :rules="[ val => val && val.length > 0 || 'Please type something']"
      />
          <q-input
        filled
        v-model="pass"
        label="Enter password"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"
      />
          <q-input
        filled
        v-model="pass2"
        label="Confirm password"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"
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
  name: 'PageIndex',
  data: function () {
    return {
      pass: '',
      pass2: '',
      email: '',
      firstname: '',
      lastname: '',
      commonName: '',
      nameSelect: 'endEntity'
    }
  },
  methods: {
    onSubmit () {
    }
  }
}
</script>
