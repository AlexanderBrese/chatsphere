<template>
  <v-layout>
    <!-- Navigationbar -->
    <v-navigation-drawer
      v-model="drawer"
      app
      fixed
      floating>
      <navigation :disable-resize-watcher="true"/>
    </v-navigation-drawer>
    <!-- Toolbar -->
    <v-toolbar
      fixed
      dark
      color="primary"
      app>
      <v-toolbar-side-icon @click.native="drawer = !drawer"/>
      <v-toolbar-title>{{ title | uppercase }}</v-toolbar-title>
    </v-toolbar>
    <v-content>
      <v-slide-y-transition mode="out-in">
        <v-layout column>
          <v-layout
            column
            align-center>
            <img
              src="@/assets/logo.png"
              alt="Vuetify.js"
              class="mb-5">
            <blockquote>
              &#8220;{{ $t('title') }}&#8221;
              <footer>
                <small>
                  <em>&mdash;John Johnson</em>
                </small>
              </footer>
            </blockquote>
          </v-layout>
          <v-layout
            row
            justify-center
            fill-height>
            <v-flex mr-5>
              <v-list dense>
                <v-list-tile
                  v-for="link in allLinks"
                  :key="link.id">
                  <v-list-tile-content>
                    <v-list-tile-title v-text="link.url"/>
                    <v-list-tile-sub-title v-text="link.description"/>
                  </v-list-tile-content>
                </v-list-tile>
              </v-list>
            </v-flex>
            <v-flex>
              <v-form>
                <v-text-field
                  v-model="url"
                  label="URL"
                  required
                />
                <v-text-field
                  v-model="description"
                  label="Description"
                  required
                />
                <v-btn
                  @click="addLink"
                >
                  submit
                </v-btn>
              </v-form>
            </v-flex>
          </v-layout>
        </v-layout>
      </v-slide-y-transition>
    </v-content>
  </v-layout>
</template>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="stylus" scoped>
  img
    width 100px
</style>
<script>
/* eslint-disable no-console */

import Navigation from '@/modules/navigation/';
import gql from 'graphql-tag';

const GET_ALL_LINKS = gql`
  query allLinks {
    allLinks {
      id
      url
      description
    }
  }
`;

const CREATE_LINK = gql`
  mutation createLink($createLinkInput: CreateLinkInput!) {
    createLink(createLinkInput: $createLinkInput) {
      id
      url
      description
    }
  }
`;

  /*
  Please always set recipient and sender in the subscriptions queries
  as this is needed for validation by the backend.
  */
const LINK_ADDED = gql`
  subscription linkAdded {
    linkAdded {
      ... on LinkAddedEvent {
          link {
            id
            url
            description
          }
      }
      sender
      recipients
    }
  }
`;

export default {
  components: { Navigation },
  data() {
    return {
      allLinks: [],
      url: '',
      description: '',
      drawer: false,
      title: 'HelloWorld',
    };
  },
  methods: {
    addLink() {
      const [url, description] = [this.url, this.description];
      this.url = '';
      this.description = '';

      try {
        this.$apollo.mutate({
          mutation: CREATE_LINK,
          variables: {
            createLinkInput: {
              url,
              description,
            },
          },
          update: (store, { data: { createLink } }) => {
            // Add to all links list
            const data = store.readQuery({ query: GET_ALL_LINKS });
            data.allLinks.push(createLink);
            store.writeQuery({
              query: GET_ALL_LINKS,
              data,
            });
          },
          optimisticResponse: {
            __typename: 'Mutation',
            createLink: {
              __typename: 'Link',
              id: null,
              url,
              description,
            },
          },
        });
      } catch (e) {
        // eslint-disable-next-line
          console.error(e);
        this.url = url;
        this.description = description;
      }
    },
  },
  apollo: {
    allLinks: {
      query: GET_ALL_LINKS,
      subscribeToMore: {
        document: LINK_ADDED,
        // Mutate the previous result
        updateQuery: (previousResult, { subscriptionData }) => {
          // Here, return the new result from the previous with the new data
          const newLink = subscriptionData.data.linkAdded.link;
          newLink.url += ' by ';
          newLink.url += subscriptionData.data.linkAdded.sender;
          newLink.description += ' at ';
          newLink.description += new Date();
          return {
            allLinks: [
              ...previousResult.allLinks,
              newLink,
            ],
          };
        },
      },
    },
  },
};
</script>
