<template>
<q-page padding>
  <div class="text-h4 text-primary q-pb-md "> Create new certificate:</div>
  <div style="max-width: 300px" >
    <q-form @submit="onSubmit" class="q-gutter-md ">
       <div class="row" style="width:600px">
      <q-select
        filled
        v-model="certificate.certificateType"
        :options="typeOptions"
        label="Certificate type"
        style="width:300px"
      />
      <q-select
        class="q-pl-md"
        filled
        v-model="certificate.issuerType"
        :options="typeOptions"
        label="Issuer type"
        style="width:300px"
      />
      </div>
      <q-btn-toggle
        v-model="nameSelect"
        push
        glossy
        toggle-color="primary"
        :options="[
          {label: 'Common name', value: 'common'},
          {label: 'First/Last name', value: 'firstlast'},
        ]"
      />
      <q-input
        filled
        v-model="certificate.commonName"
        label="Common name"
        lazy-rules
        :rules="[(val) => (val && val.length > 0) || 'Please type something']"
        v-if="nameSelect==='common'"
      />
       <div class="row" style="width:600px"  v-else>
         <div class="col">
       <q-input
        filled
        v-model="certificate.firstName"
        label="First name"
        lazy-rules
        :rules="[(val) => (val && val.length > 0) || 'Please type something']"
      />
         </div>
         <div class="col q-pl-md">
       <q-input
        filled
        v-model="certificate.lastName"
        label="Last name"
        lazy-rules
        :rules="[(val) => (val && val.length > 0) || 'Please type something']"
      />
         </div>
       </div>
      <div style="width:600px" class="row ">
        <div class="col">
        <q-input
          filled
          v-model="certificate.organization"
          label="Organization"
          lazy-rules
          :rules="[(val) => (val && val.length > 0) || 'Please type something']"
        />
        </div>
        <div class="col q-pl-md">
        <q-input
          filled
          v-model="certificate.organizationUnit"
          label="Organization unit"
          lazy-rules
          :rules="[(val) => (val && val.length > 0) || 'Please type something']"
        />
        </div>
      </div>
      <q-input
        filled
        v-model="certificate.country"
        label="Country"
        lazy-rules
        :rules="[(val) => (val && val.length > 0) || 'Please type something']"
      />
      <q-input
        filled
        v-model="certificate.email"
        label="Email"
        lazy-rules
        :rules="[(val) => (val && val.length > 0) || 'Please type something']"
      />
      <div class="row " style="width:600px">
      <div class="col " style="max-width: 300px">
        <q-input filled v-model="certificate.startDate" label="Valid from" readonly>
          <template v-slot:append>
            <q-icon name="event" class="cursor-pointer">
              <q-popup-proxy
                ref="qDateProxy"
                transition-show="scale"
                transition-hide="scale"
              >
                <q-date v-model="certificate.startDate" mask="YYYY-MM-DD">
                  <div class="row items-center justify-end">
                    <q-btn v-close-popup label="Close" color="primary" flat />
                  </div>
                </q-date>
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
      </div>
      <div class="col q-pl-md " style="max-width: 300px">
        <q-input filled v-model="certificate.endDate" label="Valid to" readonly>
          <template v-slot:append>
            <q-icon name="event" class="cursor-pointer">
              <q-popup-proxy
                ref="qDateProxy"
                transition-show="scale"
                transition-hide="scale"
              >
                <q-date v-model="certificate.endDate" mask="YYYY-MM-DD">
                  <div class="row items-center justify-end">
                    <q-btn v-close-popup label="Close" color="primary" flat />
                  </div>
                </q-date>
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
      </div>
      </div>
      <q-btn
        label="Find valid issuers"
        v-on:click="getIssuers"
        color="primary"
      />
      <q-select filled v-model="issuer" v-bind:readonly="issuerDisabled" :options="issuers" label="Issuer" />
      <div>
        <q-btn label="Create certificate" type="submit" color="primary" />
      </div>
    </q-form>
  </div>
</q-page>
</template>

<script>
export default {
  name: 'PageIndex',
  data: function () {
    return {
      issuers: [],
      certificate: {
        commonName: '',
        firstName: '',
        lastName: '',
        issuerSerialNumber: '',
        startDate: '',
        endDate: '',
        organization: '',
        organizationUnit: '',
        certificateType: '',
        country: '',
        email: '',
        issuerType: ''
      },
      issuer: null,
      typeOptions: ['root', 'intermediate', 'endEntity'],
      issuerTypeOptions: ['service', 'subsystem', 'user'],
      object: '',
      issuerDisabled: true,
      nameSelect: 'common'
    }
  },
  methods: {
    onSubmit () {
      if (this.certificate.certificateType !== 'root') {
        this.certificate.issuerSerialNumber = this.issuer.value
      }
      this.$axios.post('https://localhost:8085/api/certificate/create', this.certificate)
        .then(response => {
          console.log(response)
          this.$q.notify({
            type: 'positive',
            message: 'Certificate succesfully created.'
          })
            .catch(err => {
              console.log(err)
              this.$q.notify({
                type: 'negative',
                message: 'Error.'
              })
            })
        })
    },
    getIssuers () {
      this.$axios
        .post('https://localhost:8085/api/certificate/validIssuers', {
          startDate: this.certificate.startDate,
          endDate: this.certificate.endDate
        })
        .then((response) => {
          if (response.data.length === 0) {
            this.$q.notify({
              type: 'warning',
              message: 'No avaliable issuers,try another time span.'
            })
            this.issuerDisabled = true
            return
          }
          response.data.forEach((element) => {
            this.issuerDisabled = false
            var certificate = {}
            certificate.label = element.subject
            certificate.value = element.serialNumber
            this.issuers.push(certificate)
          })
        })
        .catch(err => {
          console.log(err)
          this.$q.notify({
            type: 'negative',
            message: 'Error.'
          })
        })
    }
  }
}
</script>
