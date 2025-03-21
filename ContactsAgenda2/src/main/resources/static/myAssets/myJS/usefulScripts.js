		function clearAllText() {
			document.querySelectorAll("#columnsContainer input[type='text']").forEach(input => input.value = "");
		}
		
		function showDeletionConfirmDialog(contactId) {
			const dialog = document.getElementById("deletionConfirmDialog-" + contactId);
			if (dialog) {
				dialog.style.display = 'flex';
			} else {
				console.error(`Confirmation dialog for contact ID ${contactId} not found.`);
			}
		}
		function showUserDeletionConfirmDialog(userId) {
			const dialog = document.getElementById("deletionConfirmDialog-" + userId);
			if (dialog) {
				dialog.style.display = 'flex';
			} else {
				console.error(`Confirmation dialog for user ID ${userId} not found.`);
			}
		}

		function confirmDeletion(contactId) {
			const form = document.getElementById("deleteForm-" + contactId);
			if (form) {
				form.submit();
			} else {
				console.error(`Delete form for contact ID ${contactId} not found.`);
			}
		}
		
		function confirmUserDeletion(userId) {
			const form = document.getElementById("deleteForm-" + userId);
			if (form) {
				form.submit();
			} else {
				console.error(`Delete form for user ID ${userId} not found.`);
			}
		}

		function cancelDeletion(contactId) {
			const dialog = document.getElementById("deletionConfirmDialog-" + contactId);
			if (dialog) {
				dialog.style.display = 'none';
			} else {
				console.error(`Confirmation dialog for contact ID ${contactId} not found.`);
			}
		}
				
		function cancelUserDeletion(userId) {
			const dialog = document.getElementById("deletionConfirmDialog-" + userId);
			if (dialog) {
				dialog.style.display = 'none';
			} else {
				console.error(`Confirmation dialog for user ID ${userId} not found.`);
			}
		}
				
		function showModifyConfirmDialog(contactId) {
			const dialog = document.getElementById("modifyConfirmDialog-" + contactId);
			if (dialog) {
				dialog.style.display = 'flex';
			} else {
				console.error(`Update dialog for contact ID ${contactId} not found.`);
			}
		}
		
		function confirmModify(contactId) {
			const form = document.getElementById("modifyForm-" + contactId);
			if (form) {
				if(form.reportValidity()){
				form.submit();
				} else {
					console.error("Form validation failed, please check the input fields.");
				}
			} else {
				console.error(`Update form for contact ID ${contactId} not found.`);
			}
		}

		function cancelModify(contactId) {
			const dialog = document.getElementById("modifyConfirmDialog-" + contactId);
			if (dialog) {
				dialog.style.display = 'none';
			} else {
				console.error(`Update dialog for contact ID ${contactId} not found.`);
			}
		}

		function showUserModifyConfirmDialog(userId) {
			const dialog = document.getElementById("modifyConfirmDialog-" + userId);
			if (dialog) {
				dialog.style.display = 'flex';
			} else {
				console.error(`Update dialog for user ID ${userId} not found.`);
			}
		}
					
		function confirmUserModify(userId) {
			const form = document.getElementById("modifyForm-" + userId);
			if (form) {
				if(form.reportValidity()){
				form.submit();
				} else {
					console.error("Form validation failed, please check the input fields.");
				}
			} else {
				console.error(`Update form for user ID ${userId} not found.`);
			}
		}

		function cancelUserModify(userId) {
			const dialog = document.getElementById("modifyConfirmDialog-" + userId);
			if (dialog) {
				dialog.style.display = 'none';
			} else {
				console.error(`Update dialog for user ID ${userId} not found.`);
			}
		}
				
		function showPasswordResetDialog(userId) {
			const dialog = document.getElementById("resetPasswordDialog-" + userId);
			if (dialog) {
				dialog.style.display = 'flex';
			} else {
				console.error(`Password reset dialog for user ID ${userId} not found.`);
			}
		}
				
		function confirmPasswordReset(userId) {
			const form = document.getElementById("resetPasswordForm-" + userId);
			if (form) {
				if(form.reportValidity()){
				form.submit();
				} else {
					console.error("Form validation failed, please check the input fields.");
				}
			} else {
				console.error(`Password reset form for user ID ${userId} not found.`);
			}
		}
		
		function cancelPasswordReset(userId) {
			const dialog = document.getElementById("resetPasswordDialog-" + userId);
			if (dialog) {
				dialog.style.display = 'none';
			} else {
				console.error(`Password reset dialog for user ID ${userId} not found.`);
			}
		}
				
					
		function showActivationDialog(userId) {
			const dialog = document.getElementById("activationDialog-" + userId);
			if (dialog) {
				dialog.style.display = "flex";
			} else {
				console.error(`Activation dialog for user ID ${userId} not found.`);
			}
		}
		
		function confirmActivation(userId) {
			const form = document.getElementById("activationForm-" + userId);
			if (form) {
				if(form.reportValidity()){
				form.submit();
				} else {
					console.error("Form validation failed, please check the input fields.");
				}			
				} else {
				console.error(`Activation dialog for user ID ${userId} not found.`);
			}
		}
		
		function cancelActivation(userId) {
			const dialog = document.getElementById("activationDialog-" + userId);
			if (dialog) {
				dialog.style.display = "none";
			} else {
				console.error(`Activation dialog for user ID ${userId} not found.`);
			}
		}
		
		function confirmNewPassword(userId) {
			const form = document.getElementById("setPasswordForm-" + userId);
			if (form) {
				if(form.reportValidity()){
				form.submit();
				} else {
					console.error("Form validation failed, please check the input fields.");
				}
			} else {
				console.error(`New password form for user ID ${userId} not found.`);
			}
		}
		
		function cancelNewPassword(userId) {
			const dialog = document.getElementById("setPasswordForm-" + userId);
			if (dialog) {
				dialog.style.display = 'none';
			} else {
				console.error(`New password form for user ID ${userId} not found.`);
			}
		}

		function showCreationDialog() {
			const dialog = document.getElementById("createUserDialog");
			if (dialog) {
				dialog.style.display = "flex";
			} else {
				console.error("Something happened when creating a dialog");
			}
		}
		
		function confirmCreation() {
			const form = document.getElementById("newUserForm");
			if (form) {
				if (form.reportValidity()){
					form.submit();
				} else {
					console.error("Form validation error, please check the input field");
				}
			} else {
				console.error("Something happened when confirming the creation of a user");
			}
		}
		
		function cancelCreation() {
			const dialog = document.getElementById("createUserDialog");
			if (dialog) {
				dialog.style.display = "none";
			} else {
				console.error("Something happened when cancelling the creation of a user");
			}
		}
					
		function showResetDialog() {
			const dialog = document.getElementById("forgottenPasswordDialog");
			if (dialog) {
				dialog.style.display = "flex";
			} else {
				console.error("Something happened when creating a dialog to reset the password");
			}
		}
		
		function confirmReset() {
			const form = document.getElementById("forgottenPasswordForm");
			if (form) {
				if (form.reportValidity()){
					form.submit();
				} else {
					console.error("Form validation error, please check the input field");
				}			
			} else {
				console.error("Something happened when confirming the resetting of a password");
			}
		}
		
		function cancelReset() {
			const dialog = document.getElementById("forgottenPasswordDialog");
			if (dialog) {
				dialog.style.display = "none";
			} else {
				console.error("Something happened when cancelling the resetting of a password");
			}
		}		
					
		function showForgottenDialog() {
			const dialog = document.getElementById("forgottenUsernameDialog");
			if (dialog) {
				dialog.style.display = "flex";
			} else {
				console.error("Something happened when creating a dialog to remember the user");
			}
		}
		
		function confirmForgotten() {
			const form = document.getElementById("forgottenUsernameForm");
			if (form) {
				if (form.reportValidity()){
					form.submit();
				} else {
					console.error("Form validation error, please check the input field");

				}			
			} else {
				console.error("Something happened when confirming the resetting of a password");
			}
		}
		
		function cancelForgotten() {
			const dialog = document.getElementById("forgottenUsernameDialog");
			if (dialog) {
				dialog.style.display = "none";
			} else {
				console.error("Something happened when cancelling the remembering of a username");
			}
		}