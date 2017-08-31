package com.catlbattery.alm.vo;

public class ViewIssueOption {

	private boolean showBranches;

	private boolean showHistory;

	private boolean showLabels;

	private boolean showRichContent;

	private boolean showTestResults;

	private boolean showAttachmentDetails;

	public boolean isShowBranches() {
		return showBranches;
	}

	public void setShowBranches(boolean showBranches) {
		this.showBranches = showBranches;
	}

	public boolean isShowHistory() {
		return showHistory;
	}

	public void setShowHistory(boolean showHistory) {
		this.showHistory = showHistory;
	}

	public boolean isShowLabels() {
		return showLabels;
	}

	public void setShowLabels(boolean showLabels) {
		this.showLabels = showLabels;
	}

	public boolean isShowRichContent() {
		return showRichContent;
	}

	public void setShowRichContent(boolean showRichContent) {
		this.showRichContent = showRichContent;
	}

	public boolean isShowTestResults() {
		return showTestResults;
	}

	public void setShowTestResults(boolean showTestResults) {
		this.showTestResults = showTestResults;
	}

	public boolean isShowAttachmentDetails() {
		return showAttachmentDetails;
	}

	public void setShowAttachmentDetails(boolean showAttachmentDetails) {
		this.showAttachmentDetails = showAttachmentDetails;
	}

}
