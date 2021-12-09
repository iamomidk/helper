package com.iamomidk.helper.constants

object Constants {

	// region TIMEOUT
	const val CONNECT_TIMEOUT: Long = 5000
	const val READ_TIMEOUT: Long = 7000
	const val WRITE_TIMEOUT: Long = 1000
	// endregion

	// region responseCode
	const val CODE_400 = 400                                    // BAD REQUEST
	const val CODE_401 = 401                                    // UNAUTHORIZED
	const val CODE_402 = 402                                    // PAYMENT REQUIRED
	const val CODE_403 = 403                                    // FORBIDDEN
	const val CODE_404 = 404                                    // NOT FOUND
	const val CODE_406 = 406                                    // NOT ACCEPTABLE
	const val CODE_409 = 409                                    // CONFLICT
	const val CODE_426 = 426                                    // UPGRADE REQUIRED
	// endregion

	// region messages
	const val ok = "[OK]"
	const val unexpected = "[UNEXPECTED ERROR]"
	const val badInput = "[BAD INPUT]"
	const val regexError = "[REGEX ERROR]"
	const val notFound404 = "[404 Not Found]"
	const val accessBlocked = "[ACCESS BLOCKED]"
	const val redirection = "[REDIRECTION]"
	const val badTimestamp = "[BAD TIMESTAMP]"
	const val methodNotAllowed = "[METHOD NOT ALLOWED]"
	const val possibleAttack = "[POSSIBLE ATTACK]"
	const val encryptionAtRisk = "[ENCRYPTION AT RISK]"
	const val outOfDate = "[OUT OF DATE]"
	const val badToken = "[BAD TOKEN]"
	const val mustNotHappen = "[MUST NOT HAPPEN]"
	const val inactiveUser = "[INACTIVE_USER]"
	const val suspendedUser = "[SUSPENDED USER]"
	const val alreadyExists = "[ALREADY EXISTS]"
	const val alreadyVerified = "[ALREADY VERIFIED]"
	const val alreadyUsed = "[ALREADY USED]"
	const val noToken = "[NO TOKEN]"
	const val tokenExpired = "[TOKEN EXPIRED]"
	const val accountNotFound = "[ACCOUNT NOT FOUND]"
	const val referralNotFound = "[REFERRAL NOT FOUND]"
	const val subscriptionExpired = "[SUBSCRIPTION_EXPIRED]"
	const val repetitivePassword = "[REPETITIVE PASSWORD]"
	// endregion

	// region baseUrl
	const val BASE_URL = "https://YOUR API ADDRESS/"
	// endregion

	//region SharedPreferences
	const val SHP_KEY = "Your SHP FILE KEY GOES HERE"
	const val SHP_DARK_MOOD = "SHP_DARK_MOOD"
	const val SHP_INTRO_PLAYED = "SHP_INTRO_PLAYED"
	const val SHP_SERVER_ID = "SHP_SERVER_ID"
	// endregion

	const val SHARE_VIA = ""

}