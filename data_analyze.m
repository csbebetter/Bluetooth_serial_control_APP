
% position = [0.15 0.2 0.75 0.6];
yyaxis left
plot(test(:,1)-test(1,1).*ones(88,1),test(:,2));hold on

yyaxis right
plot(test(:,1)-test(1,1).*ones(88,1),test(:,3));
xlabel('Time(ms)');
h = legend('distance','angle');
% set(h,'box','off');
% set(gca,'Linewidth',2,'Fontsize',20,'box','on');
