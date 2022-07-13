/* eslint-disable */
// This file contains sample queries for review/debug purposes.
// Copy a section on the bottom of `main.js`,
// Append `import gql from 'graphql-tag';` to the top of the file
// Run the client side
// chat(id: Int!): Chat

const QUERY = gql`
  query chat($input: Int!) {
    chat(id: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
        author {
          id
          phone
          status
          displayName
          createdAt
        }
        date
        to {
          id
        }
      }
    }
  }
`;

apolloClient.query({
  query: QUERY,
  variables: {
    input: 5,
  },
}).then(({ data }) => console.log(data));

// chats(amount: Int): [Chat]
const QUERY2 = gql`
  query chats($input: Int) {
    chats(amount: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
        author {
          id
          phone
          status
          displayName
          createdAt
        }
        date
        to {
          id
        }
      }
    }
  }
`;

apolloClient.query({
  query: QUERY2,
  variables: {
    input: 5,
  },
}).then(({ data }) => console.log(data));

// updateChatNotificationSettings(
// updateChatNotificationSettingsInput: UpdateChatNotificationSettingsInput!): Chat,
const QUERY3 = gql`
  mutation updateChatNotificationSettings($updateChatNotificationSettingsInput: UpdateChatNotificationSettingsInput!) {
    updateChatNotificationSettings(updateChatNotificationSettingsInput: $updateChatNotificationSettingsInput){
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
        author {
          id
          phone
          status
          displayName
          createdAt
        }
        date
        to {
          id
        }
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY3,
  variables: {
    updateChatNotificationSettingsInput: {
      id: 5,
      chatId: 12,
    },
  },
}).then(({ data }) => console.log(data));

// leaveChat(leaveChatInput: LeaveChatInput!): User,
const QUERY4 = gql`
  mutation leaveChat($leaveChatInput: LeaveChatInput!) {
    leaveChat(leaveChatInput: $leaveChatInput) {
      id
      phone
      status
      displayName
      createdAt
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY4,
  variables: {
    leaveChatInput: {
      id: 5,
      chatId: 12,
    },
  },
}).then(({ data }) => console.log(data));

// groupChatByName(name: String!): GroupChat

const QUERY5 = gql`
  query groupChatByName($input: String!) {
    groupChatByName(name: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
        author {
          id
          phone
          status
          displayName
          createdAt
        }
        date
        to {
          id
        }
      }
      isPublic
    }
  }
`;

apolloClient.query({
  query: QUERY5,
  variables: {
    input: 'die lässig verträumten',
  },
}).then(({ data }) => console.log(data));


// groupChat(id: Int!): GroupChat

const QUERY6 = gql`
  query groupChat($input: Int!) {
    groupChat(id: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
        author {
          id
          phone
          status
          displayName
          createdAt
        }
        date
        to {
          id
        }
      }
      isPublic
    }
  }
`;

apolloClient.query({
  query: QUERY6,
  variables: {
    input: 2,
  },
}).then(({ data }) => console.log(data));

// createGroupChat(createGroupChatInput: CreateGroupChatInput!): User,

const QUERY7 = gql`
  mutation createGroupChat($input: CreateGroupChatInput!) {
    createGroupChat(createGroupChatInput: $input) {
      id
      phone
      status
      displayName
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY7,
  variables: {
    input: {
      participantIds: [0, 1, 1, 2, 3, 5],
      name: 'Olaf',
      picture: 'vikinger.jpg',
    },
  },
}).then(({ data }) => console.log(data));

// addParticipant(addParticipantInput: AddParticipantInput): GroupChat,

const QUERY8 = gql`
  mutation addParticipant($input: AddParticipantInput!) {
    addParticipant(addParticipantInput: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
        author {
          id
          phone
          status
          displayName
          createdAt
        }
        date
        to {
          id
        }
      }
      isPublic
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY8,
  variables: {
    input: {
      userId: 12,
      participantId: 15,
      id: 13,
    },
  },
}).then(({ data }) => console.log(data));

// removeParticipant(removeParticipantInput: RemoveParticipantInput): GroupChat,

const QUERY9 = gql`
  mutation removeParticipant($input: RemoveParticipantInput) {
    removeParticipant(removeParticipantInput: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
        author {
          id
          phone
          status
          displayName
          createdAt
        }
        date
        to {
          id
        }
      }
      isPublic
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY9,
  variables: {
    input: {
      userId: 12,
      participantId: 15,
      id: 13,
    },
  },
}).then(({ data }) => console.log(data));

// updateName(updateNameInput: UpdateNameInput): ChatStamp,

const QUERY10 = gql`
  mutation updateName($input: UpdateNameInput) {
    updateName(updateNameInput: $input) {
      icon
      name
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY10,
  variables: {
    input: {
      userId: 12,
      id: 13,
      name: 'New shiny name',
    },
  },
}).then(({ data }) => console.log(data));

// updatePicture(updatePictureInput: UpdatePictureInput): ChatStamp,

const QUERY11 = gql`
  mutation updatePicture($input: UpdatePictureInput) {
    updatePicture(updatePictureInput: $input) {
      icon
      name
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY11,
  variables: {
    input: {
      userId: 12,
      id: 13,
      picture: 'pic.png',
    },
  },
}).then(({ data }) => console.log(data));

// grantPrivileges(grantPrivilegesInput: GrantPrivilegesInput): GroupChat,

const QUERY12 = gql`
  mutation grantPrivileges($input: GrantPrivilegesInput) {
    grantPrivileges(grantPrivilegesInput: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
        author {
          id
          phone
          status
          displayName
          createdAt
        }
        date
        to {
          id
        }
      }
      isPublic
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY12,
  variables: {
    input: {
      userId: 12,
      id: 13,
      participantId: 5,
    },
  },
}).then(({ data }) => console.log(data));

// message(id: Int!): Message

const QUERY13 = gql`
  query message($input: Int!) {
    message(id: $input) {
      id
      author {
        id
        phone
        status
        displayName
      }
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.query({
  query: QUERY13,
  variables: {
    input: 5,
  },
}).then(({ data }) => console.log(data));

// removeMessage(removeMessageInput: RemoveMessageInput!): Chat,

const QUERY14 = gql`
  mutation removeMessage($input: RemoveMessageInput!) {
    removeMessage(removeMessageInput: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY14,
  variables: {
    input: {
      authorId: 5,
      id: 1,
      chatId: 2,
    },
  },
}).then(({ data }) => console.log(data));

// audioMessage(id: Int!): AudioMessage

const QUERY15 = gql`
  query audioMessage($input: Int!) {
    audioMessage(id: $input) {
      id
      author {
        id
        phone
        status
        displayName
      }
      text
      url
      date
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.query({
  query: QUERY15,
  variables: {
    input: 5,
  },
}).then(({ data }) => console.log(data));

// createAudioMessage(createMessageInput: CreateMessageInput!): Chat,

const QUERY15b = gql`
  mutation createAudioMessage($input: CreateMessageInput!) {
    createAudioMessage(createMessageInput: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY15b,
  variables: {
    input: {
      text: 'Hey check out this new track',
      fileName: 'wave.wav',
      encoded: 'AAAD+AAAABRtZWFzAAAEDAAAACR0ZWNoAAAEMAAAAAxyV',
      chatId: 12,
    },
  },
}).then(({ data }) => console.log(data));

// updateAudioMessage(updateMessageInput: UpdateMessageInput!): AudioMessage,

const QUERY16 = gql`
  mutation updateAudioMessage($input: UpdateMessageInput!) {
    updateAudioMessage(updateMessageInput: $input) {
      id
      author {
        id
        phone
        status
        displayName
      }
      text
      url
      date
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY16,
  variables: {
    input: {
      id: 12,
      text: 'wave.wav',
    },
  },
}).then(({ data }) => console.log(data));

// pictureMessage(id: Int!): PictureMessage

const QUERY17 = gql`
  query pictureMessage($input: Int!) {
    pictureMessage(id: $input) {
      id
      author {
        id
        phone
        status
        displayName
      }
      date
      text
      url
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.query({
  query: QUERY17,
  variables: {
    input: 5,
  },
}).then(({ data }) => console.log(data));

// createPictureMessage(createMessageInput: CreateMessageInput!): Chat,

const QUERY18 = gql`
  mutation createPictureMessage($input: CreateMessageInput!) {
    createPictureMessage(createMessageInput: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      notify {
        push
      }
      log {
        id
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY18,
  variables: {
    input: {
      text: 'Last day was great :)',
      fileName: 'us-together.png',
      encoded: 'RdBF2UXiReuF9IX9xgbGEAYZRiKGK8Y1Rj6GSAZRRlrGZEZtxndGgQaKhpRGncanh',
      chatId: 12,
    },
  },
}).then(({ data }) => console.log(data));

// updatePictureMessage(updateMessageInput: UpdateMessageInput!): PictureMessage,

const QUERY19 = gql`
  mutation updatePictureMessage($input: UpdateMessageInput!) {
    updatePictureMessage(updateMessageInput: $input) {
      id
      author {
        id
        phone
        status
        displayName
      }
      text
      url
      date
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY19,
  variables: {
    input: {
      id: 12,
      text: 'changed my mind',
    },
  },
}).then(({ data }) => console.log(data));

// notificationMessage(id: Int!): NotificationMessage

const QUERY20 = gql`
  query notificationMessage($input: Int!) {
    notificationMessage(id: $input) {
      id
      author {
        id
        phone
        status
        displayName
      }
      recipient {
        id
        phone
        status
        displayName
      }
      text
      notify {
        push
      }
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.query({
  query: QUERY20,
  variables: {
    input: 5,
  },
}).then(({ data }) => console.log(data));

// createNotificationMessage(
//  createNotificationMessageInput: CreateNotificationMessageInput!): Chat,

const QUERY21 = gql`
  mutation createNotificationMessage($input: CreateNotificationMessageInput!) {
    createNotificationMessage(createNotificationMessageInput: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      log {
        id
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY21,
  variables: {
    input: {
      text: 'A notification!',
      notify: {
        push: 'NOTIFY',
      },
      recipientName: 'root',
      chatId: 12,
    },
  },
}).then(({ data }) => console.log(data));

// updateNotificationMessage(
//    updateNotificationMessageInput: UpdateNotificationMessageInput!): NotificationMessage,

const QUERY22 = gql`
  mutation updateNotificationMessage($input: UpdateNotificationMessageInput!) {
    updateNotificationMessage(updateNotificationMessageInput: $input) {
      id
      author {
        id
        phone
        status
        displayName
      }
      recipient {
        id
        phone
        status
        displayName
      }
      notify {
        push
      }
      text
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY22,
  variables: {
    input: {
      id: 12,
      authorId: 5,
      text: 'Update this please',
      notify: {
        push: 'NOTIFY',
      },
      recipientId: 15,
    },
  },
}).then(({ data }) => console.log(data));

// videoMessage(id: Int!): VideoMessage

const QUERY23 = gql`
  query videoMessage($input: Int!) {
    videoMessage(id: $input) {
      id
      date
      author {
        id
        phone
        status
        displayName
      }
      text
      url
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.query({
  query: QUERY23,
  variables: {
    input: 5,
  },
}).then(({ data }) => console.log(data));

// createVideoMessage(createMessageInput: CreateMessageInput!): Chat,

const QUERY24 = gql`
  mutation createVideoMessage($input: CreateMessageInput!) {
    createVideoMessage(createMessageInput: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      log {
        id
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY24,
  variables: {
    input: {
      text: 'Check out this new youtube poop haha',
      fileName: 'bonkerz.mp4',
      encoded: 'CYIMQg8CEcIUghdSGhIc4h+yInIlUigiKvIt0jCiM4I2YjlCPCI',
      chatId: 12,
    },
  },
}).then(({ data }) => console.log(data));

// updateVideoMessage(updateMessageInput: UpdateMessageInput!): VideoMessage,

const QUERY25 = gql`
  mutation updateVideoMessage($input: UpdateMessageInput!) {
    updateVideoMessage(updateMessageInput: $input) {
      id
      date
      author {
        id
        phone
        status
        displayName
      }
      text
      url
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY25,
  variables: {
    input: {
      id: 12,
      text: 'Changed my mind',
    },
  },
}).then(({ data }) => console.log(data));

// documentMessage(id: Int!): DocumentMessage

const QUERY26 = gql`
  query documentMessage($input: Int!) {
    documentMessage(id: $input) {
      id
      date
      author {
        id
        phone
        status
        displayName
      }
      text
      url
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.query({
  query: QUERY26,
  variables: {
    input: 5,
  },
}).then(({ data }) => console.log(data));

// createDocumentMessage(createMessageInput: CreateMessageInput!): Chat,

const QUERY27 = gql`
  mutation createDocumentMessage($input: CreateMessageInput!) {
    createDocumentMessage(createMessageInput: $input) {
      id
      stamp {
        icon
        name
      }
      participants {
        userId
        hasPrivileges
      }
      log {
        id
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY27,
  variables: {
    input: {
      text: 'Hey would you please review this document?',
      fileName: 'notavirus.pdf',
      encoded: 'AAAAAAAAAasdasdAAAAA1AAAAA',
      chatId: 12,
    },
  },
}).then(({ data }) => console.log(data));

// updateDocumentMessage(updateMessageInput: UpdateMessageInput!): DocumentMessage,

const QUERY28 = gql`
  mutation updateDocumentMessage($input: UpdateMessageInput!) {
    updateDocumentMessage(updateMessageInput: $input) {
      id
      date
      author {
        id
        phone
        status
        displayName
      }
      text
      url
      to {
        id
        participants {
          userId
          hasPrivileges
        }
        log {
          id
        }
        notify {
          push
        }
        stamp {
          icon
          name
        }
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY28,
  variables: {
    input: {
      id: 12,
      text: 'Changed my mind',
    },
  },
}).then(({ data }) => console.log(data));

// updatePassword(updatePasswordInput: UpdatePasswordInput!): Account,

const QUERY29 = gql`
  mutation updatePassword($input: UpdatePasswordInput!) {
    updatePassword(updatePasswordInput: $input) {
      id
      username
      email
      user {
        displayName
        status
      }
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY29,
  variables: {
    input: {
      password: 'newPassword',
    },
  },
}).then(({ data }) => console.log(data));


// updateEmail(updateEmailInput: UpdateEmailInput): Account

const QUERY30 = gql`
mutation updateEmail($input: UpdateEmailInput!) {
  updateEmail(updateEmailInput: $input) {
    id
    username
    email
    user {
      displayName
      status
    }
  }
}
`;

apolloClient.mutate({
mutation: QUERY30,
variables: {
  input: {
    email: 'new@mail.de',
  },
},
}).then(({ data }) => console.log(data));

// updateNotificationSettings(updateNotificationInput: UpdateNotificationInput!): User

const QUERY31 = gql`
  mutation updateNotificationSettings($input: UpdateNotificationInput!) {
    updateNotificationSettings(updateNotificationInput: $input) {
      displayName
      status
    }
  }
`;

apolloClient.mutate({
  mutation: QUERY31,
  variables: {
    input: {
      notify: {
        push: 'MUTE',
      },
    },
  },
}).then(({ data }) => console.log(data));

// reportAccount(reportAccountInput: ReportAccountInput!): Boolean

const QUERY31 = gql`
mutation reportAccount($input: ReportAccountInput!) {
  reportAccount(reportAccountInput: $input)
}
`;

apolloClient.mutate({
  mutation: QUERY31,
  variables: {
    input: {
      reportedUsername: 'admin',
      reason: 'for being a noob',
    },
  },
}).then(({ data }) => console.log(data));

// blockAccount(blockAccountInput: BlockAccountInput!): Boolean

const QUERY32 = gql`
mutation blockAccount($input: BlockAccountInput!) {
  blockAccount(blockAccountInput: $input)
}
`;

apolloClient.mutate({
  mutation: QUERY32,
  variables: {
    input: {
      blockedUsername: 'admin',
    },
  },
}).then(({ data }) => console.log(data));

// unblockAccount(unblockAccountInput: UnblockAccountInput!): Boolean

const QUERY32 = gql`
mutation unblockAccount($input: UnblockAccountInput!) {
  unblockAccount(unblockAccountInput: $input)
}
`;

apolloClient.mutate({
  mutation: QUERY32,
  variables: {
    input: {
      unblockedUsername: 'admin',
    },
  },
}).then(({ data }) => console.log(data));

// updateAvatar(updateAvatarInput: UpdateAvatarInput!): User

const QUERY33 = gql`
mutation updateAvatar($input: UpdateAvatarInput!) {
  updateAvatar(updateAvatarInput: $input) {
    status
    displayName
  }
}
`;

apolloClient.mutate({
  mutation: QUERY33,
  variables: {
    input: {
      avatar: 'abb87F9810E8AC2B2AD7EF5423F381D7641778EC8A63B95EA5A388483CE98E69',
    },
  },
}).then(({ data }) => console.log(data));

// updatePhone(updatePhoneInput: UpdatePhoneInput!): User

const QUERY34 = gql`
mutation updatePhone($input: UpdatePhoneInput!) {
  updatePhone(updatePhoneInput: $input) {
    status
    displayName
  }
}
`;

apolloClient.mutate({
  mutation: QUERY34,
  variables: {
    input: {
      phone: '(0) 1337 1337',
    },
  },
}).then(({ data }) => console.log(data));

// updateStatus(updateStatusInput: UpdateStatusInput!): User

const QUERY35 = gql`
mutation updateStatus($input: UpdateStatusInput!) {
  updateStatus(updateStatusInput: $input) {
    status
    displayName
  }
}
`;

apolloClient.mutate({
  mutation: QUERY35,
  variables: {
    input: {
      status: 'Grooovy',
    },
  },
}).then(({ data }) => console.log(data));