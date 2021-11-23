# TicketManagementSystem

## Projects:

### 1.TicketGenerator

1. Go into your account, use admin access to enable APIs with tokens and password/email
2. change the information in ```myLogin.properties``` to your own information
3. Run ```main.java```

### 2. MyTicketMaster_CLI


## Terminology

1. **Submitter, Requester:** 
   1. The submitter is the user who created a ticket. By default, the requester of a ticket is the submitter.
   2. However, a support agent can also create a ticket on behalf of a customer. If an agent creates a ticket through the web interface, the agent is set as the submitter.
   3. whomever is the submitter becomes the first comment author. This will hold true for all tickets created in Zendesk Support with **one exception:** If the ticket is created as a follow-up ticket (i.e., if the ticket is created using via_followup_source_id), then any submitter_id attribute is ignored. The API sets whoever created the follow-up ticket (for the API, always the authenticated user) as the first comment author
2. **Comments**: When creating a ticket, use the ```comment``` property to set the ticket description